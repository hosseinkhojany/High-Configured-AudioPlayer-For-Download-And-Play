package adams.sheek.montazeranapp.di.singletonComponent

import adams.sheek.montazeranapp.ui.classDetail.ClassDetailFragment
import adams.sheek.montazeranapp.utils.exo.DemoUtil
import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.RenderersFactory
import com.google.android.exoplayer2.TracksInfo
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.EventLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        val dataSourceFactory = DemoUtil.getDataSourceFactory(context)
        val trackSelectionParameters = DefaultTrackSelector.ParametersBuilder(context).build()
            val renderersFactory: RenderersFactory = DemoUtil.buildRenderersFactory(context, false)
            val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(dataSourceFactory!!)
            val trackSelector = DefaultTrackSelector(context)
            val player = ExoPlayer.Builder(context)
                .setRenderersFactory(renderersFactory)
                .setMediaSourceFactory(mediaSourceFactory)
                .setTrackSelector(trackSelector)
                .build()
            player.trackSelectionParameters = trackSelectionParameters
            player.setAudioAttributes(AudioAttributes.Builder().setContentType(C.CONTENT_TYPE_MUSIC).setUsage(C.USAGE_MEDIA).build(), true)
            player.setHandleAudioBecomingNoisy(true)
            player.playWhenReady = false
        return player
    }
    
}