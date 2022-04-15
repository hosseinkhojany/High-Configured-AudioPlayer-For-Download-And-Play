package adams.sheek.montazeranapp.utils.exo

import adams.sheek.montazeranapp.R
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.data.repositories.TopicRepository
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.IBinder
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.util.NotificationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlayerForegroundService() : Service() {

    @Inject
    lateinit var player: ExoPlayer

    private lateinit var playerNotificationManager: PlayerNotificationManager

    private var filename = ""

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object{
        var kill = false
    }

    override fun onCreate() {
        super.onCreate()
        InRamDs.playerServiceIsRunning = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        CoroutineScope(Dispatchers.Main).launch {
            CoroutineScope(Dispatchers.IO).launch {
                while(true){
                    if (kill) kill()
                }
            }
        }

        intent?.extras?.getString("fromApp", "false").equals("true").let {
            if (it){
                playerNotificationManager = PlayerNotificationManager
                    .Builder(applicationContext, 9998, "streaming_channel")
                    .setMediaDescriptionAdapter(object : PlayerNotificationManager.MediaDescriptionAdapter{
                        override fun getCurrentContentTitle(player: Player): CharSequence {
                            var res = ""
                            player.currentMediaItem?.mediaMetadata?.description?.let {
                                res = getTitleFromPlayer(it.toString())
                                filename = getFileNameFromPlayer(it.toString())
                            }
                            return getTitleFromPlayer(res)
                        }

                        override fun createCurrentContentIntent(player: Player): PendingIntent? {
                            return null
                        }

                        override fun getCurrentContentText(player: Player): CharSequence? {
                            var res = ""
                            player.currentMediaItem?.mediaMetadata?.description?.let {
                                res = getContentFromPlayer(it.toString())
                            }
                            return res
                        }

                        override fun getCurrentSubText(player: Player): CharSequence? {
                            return super.getCurrentSubText(player)
                        }

                        override fun getCurrentLargeIcon(
                            player: Player,
                            callback: PlayerNotificationManager.BitmapCallback
                        ): Bitmap? {
                            return null
                        }
                    })
                    .setChannelImportance(NotificationUtil.IMPORTANCE_DEFAULT)
                    .setNotificationListener(object : PlayerNotificationManager.NotificationListener {
                        override fun onNotificationPosted(
                            notificationId: Int,
                            notification: Notification,
                            ongoing: Boolean
                        ) {
                            super.onNotificationPosted(notificationId, notification, ongoing)
                            if (ongoing) {
                                startForeground(notificationId, notification)
                            }else{
                                TopicRepository.saveMediaState(filename, player.currentPosition.toFloat())
                            }
                        }

                        override fun onNotificationCancelled(
                            notificationId: Int,
                            dismissedByUser: Boolean
                        ) {
                            super.onNotificationCancelled(notificationId, dismissedByUser)
                            kill()
                        }
                    })
                    .setChannelNameResourceId(R.string.app_name)
                    .setChannelDescriptionResourceId(R.string.app_name)
                    .build()
                playerNotificationManager.setColor(Color.WHITE)
                playerNotificationManager.setColorized(true)
                playerNotificationManager.setSmallIcon(R.drawable.exo_notification_small_icon)
                playerNotificationManager.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE)
                playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                playerNotificationManager.setUseChronometer(true)
                playerNotificationManager.setSmallIcon(R.drawable.download_icon)
                playerNotificationManager.setPriority(NotificationCompat.PRIORITY_DEFAULT)
                playerNotificationManager.setPlayer(player)

                val mediaSessionCompat = MediaSessionCompat(applicationContext, "MEDIA_SESSION_TAG")
                mediaSessionCompat.isActive = true
                playerNotificationManager.setMediaSessionToken(mediaSessionCompat.sessionToken)
                val mediaSessionConnector = MediaSessionConnector(mediaSessionCompat)
                val timelineQueueNavigator = object : TimelineQueueNavigator(mediaSessionCompat) {
                    override fun getMediaDescription(
                        player: Player,
                        windowIndex: Int
                    ): MediaDescriptionCompat {
                        return MediaDescriptionCompat.Builder().build()
                    }
                }
                mediaSessionConnector.setQueueNavigator(timelineQueueNavigator)
                mediaSessionConnector.setPlayer(player)

            }
        }


        return START_STICKY
    }

    private fun kill(){
        try {
            stopSelf()
            stopForeground(false)
            player.release()
            playerNotificationManager.setPlayer(null);
        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        InRamDs.playerServiceIsRunning = false
        super.onDestroy()

    }

    private fun getFileNameFromPlayer(description: String): String{
        return description.split(")-(")[2]
    }
    private fun getTitleFromPlayer(description: String): String{
        return description.split(")-(")[0]
    }
    private fun getContentFromPlayer(description: String): String{
        return description.split(")-(")[1]
    }
}