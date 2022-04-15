package adams.sheek.montazeranapp.utils.exo;

import android.app.Notification;
import android.content.Context;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.offline.Download;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.PlatformScheduler;
import com.google.android.exoplayer2.scheduler.Requirements;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationHelper;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;

import java.util.List;


import adams.sheek.montazeranapp.R;
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs;
import adams.sheek.montazeranapp.data.repositories.TopicRepository;
import dagger.hilt.android.AndroidEntryPoint;

/** A service for downloading media. */
@AndroidEntryPoint
public class DemoDownloadService extends DownloadService {

  private static final int JOB_ID = 1;
  private static final int FOREGROUND_NOTIFICATION_ID = 1;

  public DemoDownloadService() {
    super(
        FOREGROUND_NOTIFICATION_ID,
        DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
        "download_channel",
           R.string.downloading ,
        /* channelDescriptionResourceId= */ 0);
  }

  @Override
  protected DownloadManager getDownloadManager() {
    // This will only happen once, because getDownloadManager is guaranteed to be called only once
    // in the life cycle of the process.
    DownloadManager downloadManager = DemoUtil.getDownloadManager(/* context= */ this);
    DownloadNotificationHelper downloadNotificationHelper =
        DemoUtil.getDownloadNotificationHelper(/* context= */ this);
    downloadManager.addListener(
        new TerminalStateNotificationHelper(
            this, downloadNotificationHelper, FOREGROUND_NOTIFICATION_ID + 1));
    return downloadManager;
  }

  @Override
  protected Scheduler getScheduler() {
    return Util.SDK_INT >= 21 ? new PlatformScheduler(this, JOB_ID) : null;
  }

  @Override
  protected Notification getForegroundNotification(
      List<Download> downloads, @Requirements.RequirementFlags int notMetRequirements) {
    return DemoUtil.getDownloadNotificationHelper(/* context= */ this)
        .buildProgressNotification(
            /* context= */ this,
            R.drawable.ic_download,
            /* contentIntent= */ null,
            /* message= */ "در حال دانلود فایل",
            downloads,
            notMetRequirements);
  }

  /**
   * Creates and displays notifications for downloads when they complete or fail.
   *
   * <p>This helper will outlive the lifespan of a single instance of {@link DemoDownloadService}.
   * It is static to avoid leaking the first {@link DemoDownloadService} instance.
   */
  static final class TerminalStateNotificationHelper implements DownloadManager.Listener {


    private final Context context;
    private final DownloadNotificationHelper notificationHelper;

    private int nextNotificationId;

    public TerminalStateNotificationHelper(Context context, DownloadNotificationHelper notificationHelper, int firstNotificationId) {
      this.context = context.getApplicationContext();
      this.notificationHelper = notificationHelper;
      nextNotificationId = firstNotificationId;
    }

    @Override
    public void onDownloadChanged(
        DownloadManager downloadManager, Download download, @Nullable Exception finalException) {
      Notification notification;
      if (download.state == Download.STATE_COMPLETED) {
        notification =
            notificationHelper.buildDownloadCompletedNotification(
                context,
                R.drawable.ic_download_done,
                /* contentIntent= */ null,
                    "دانلود کامل شد");
        //change model status
        TopicRepository.Companion.updateTopicRecord(Util.fromUtf8Bytes(download.request.data), true, false);
      } else if (download.state == Download.STATE_FAILED) {
        notification =
            notificationHelper.buildDownloadFailedNotification(
                context,
                R.drawable.ic_download_done,
                /* contentIntent= */ null,
                    "دانلود با شکست مواجه شد");
        //change model status
        TopicRepository.Companion.updateTopicRecord(Util.fromUtf8Bytes(download.request.data), true, false);
      } else {
        return;
      }
      NotificationUtil.setNotification(context, nextNotificationId++, notification);
    }
  }
}
