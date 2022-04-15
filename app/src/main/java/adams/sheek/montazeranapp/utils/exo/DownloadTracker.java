package adams.sheek.montazeranapp.utils.exo;

import static com.google.android.exoplayer2.util.Assertions.checkNotNull;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.MediaMetadata;
import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadCursor;
import com.google.android.exoplayer2.offline.DownloadHelper;
import com.google.android.exoplayer2.offline.DownloadHelper.LiveContentUnsupportedException;
import com.google.android.exoplayer2.offline.DownloadIndex;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadRequest;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

import adams.sheek.montazeranapp.R;
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs;
import adams.sheek.montazeranapp.data.model.Topic;
import adams.sheek.montazeranapp.data.model.TopicFile;
import adams.sheek.montazeranapp.data.repositories.TopicRepository;

/** Tracks media that has been downloaded. */
public class DownloadTracker {

  /** Listens for changes in the tracked downloads. */
  public interface Listener {

    /** Called when the tracked downloads changed. */
    void onDownloadsChanged();
  }

  private static final String TAG = "DownloadTracker";

  private final Context context;
  private final HttpDataSource.Factory httpDataSourceFactory;
  private final CopyOnWriteArraySet<Listener> listeners;
  private final HashMap<Uri, Download> downloads;
  private final DownloadIndex downloadIndex;
  private final DefaultTrackSelector.Parameters trackSelectorParameters;

  @Nullable private StartDownloadDialogHelper startDownloadDialogHelper;

  public DownloadTracker(
          Context context,
          HttpDataSource.Factory httpDataSourceFactory,
          DownloadManager downloadManager) {
    this.context = context.getApplicationContext();
    this.httpDataSourceFactory = httpDataSourceFactory;
    listeners = new CopyOnWriteArraySet<>();
    downloads = new HashMap<>();
    downloadIndex = downloadManager.getDownloadIndex();
    trackSelectorParameters = DownloadHelper.getDefaultTrackSelectorParameters(context);
    downloadManager.addListener(new DownloadManagerListener());
    loadDownloads();
  }

  public void addListener(Listener listener) {
    checkNotNull(listener);
    listeners.add(listener);
  }

  public void removeListener(Listener listener) {
    listeners.remove(listener);
  }

  public boolean isDownloaded(MediaItem mediaItem) {
    @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
    return download != null && download.state != Download.STATE_FAILED;
  }

  @Nullable
  public DownloadRequest getDownloadRequest(Uri uri) {
    @Nullable Download download = downloads.get(uri);
    return download != null && download.state != Download.STATE_FAILED ? download.request : null;
  }

  public void toggleDownload(String url) {

    MediaItem mediaItem = new MediaItem.Builder().setUri(url).setMediaMetadata(new MediaMetadata.Builder().setTitle(url).build()).build();
    @Nullable Download download = downloads.get(checkNotNull(mediaItem.localConfiguration).uri);
    boolean state = false;
    if (download != null && download.state != Download.STATE_FAILED) {
      DownloadService.sendRemoveDownload(context, DemoDownloadService.class, download.request.id, /* foreground= */ true);
    } else {
      state = true;
      if (startDownloadDialogHelper != null) {
        startDownloadDialogHelper.release();
      }
      startDownloadDialogHelper = new StartDownloadDialogHelper(DownloadHelper.forMediaItem(context, mediaItem, new DefaultRenderersFactory(context), httpDataSourceFactory), mediaItem);
    }
    TopicRepository.Companion.updateTopicRecord(url, false, state);
  }

  private void loadDownloads() {
    try (DownloadCursor loadedDownloads = downloadIndex.getDownloads()) {
      while (loadedDownloads.moveToNext()) {
        Download download = loadedDownloads.getDownload();
        downloads.put(download.request.uri, download);
      }
    } catch (IOException e) {
      Log.w(TAG, "Failed to query downloads", e);
    }
  }

  private class DownloadManagerListener implements DownloadManager.Listener {

    @Override
    public void onDownloadChanged(
            DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
      downloads.put(download.request.uri, download);
      for (Listener listener : listeners) {
        listener.onDownloadsChanged();
      }
    }

    @Override
    public void onDownloadRemoved(DownloadManager downloadManager, Download download) {
      downloads.remove(download.request.uri);
      for (Listener listener : listeners) {
        listener.onDownloadsChanged();
      }
    }
  }

  private final class StartDownloadDialogHelper
          implements DownloadHelper.Callback,
          DialogInterface.OnClickListener,
          DialogInterface.OnDismissListener {

    private final DownloadHelper downloadHelper;
    private final MediaItem mediaItem;

    @Nullable private byte[] keySetId;

    public StartDownloadDialogHelper(DownloadHelper downloadHelper, MediaItem mediaItem) {
      this.downloadHelper = downloadHelper;
      this.mediaItem = mediaItem;
      downloadHelper.prepare(this);
    }

    public void release() {
      downloadHelper.release();
    }

    // DownloadHelper.Callback implementation.

    @Override
    public void onPrepared(DownloadHelper helper) {
      onDownloadPrepared(helper);
    }

    @Override
    public void onPrepareError(DownloadHelper helper, IOException e) {
      boolean isLiveContent = e instanceof LiveContentUnsupportedException;
      int toastStringId =
              isLiveContent ? R.string.download_live_unsupported : R.string.download_start_error;
      String logMessage =
              isLiveContent ? "Downloading live content unsupported" : "Failed to start download";
      Toast.makeText(context, toastStringId, Toast.LENGTH_LONG).show();
      Log.e(TAG, logMessage, e);
    }

    // DialogInterface.OnClickListener implementation.

    @Override
    public void onClick(DialogInterface dialog, int which) {
      for (int periodIndex = 0; periodIndex < downloadHelper.getPeriodCount(); periodIndex++) {
        downloadHelper.clearTrackSelections(periodIndex);
      }
      DownloadRequest downloadRequest = buildDownloadRequest();
      if (downloadRequest.streamKeys.isEmpty()) {
        // All tracks were deselected in the dialog. Don't start the download.
        return;
      }
      startDownload(downloadRequest);
    }

    // DialogInterface.OnDismissListener implementation.

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
      downloadHelper.release();
    }

    private void onDownloadPrepared(DownloadHelper helper) {
      if (helper.getPeriodCount() == 0) {
        Log.d(TAG, "No periods found. Downloading entire stream.");
        startDownload();
        downloadHelper.release();
      }
    }

    private void startDownload() {
      startDownload(buildDownloadRequest());
    }

    private void startDownload(DownloadRequest downloadRequest) {
      DownloadService.sendAddDownload(
              context, DemoDownloadService.class, downloadRequest, /* foreground= */ true);
    }

    private DownloadRequest buildDownloadRequest() {
      return downloadHelper
              .getDownloadRequest(
                      Util.getUtf8Bytes(checkNotNull(mediaItem.mediaMetadata.title.toString())))
              .copyWithKeySetId(keySetId);
    }
  }
}
