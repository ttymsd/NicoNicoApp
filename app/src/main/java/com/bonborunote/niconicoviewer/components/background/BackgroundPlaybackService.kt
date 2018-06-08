package com.bonborunote.niconicoviewer.components.background

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.arch.lifecycle.LifecycleService
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v4.media.app.NotificationCompat.DecoratedMediaCustomViewStyle
import android.support.v4.media.app.NotificationCompat.MediaStyle
import android.support.v7.graphics.Palette
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutServiceContainerBinding
import com.bonborunote.niconicoviewer.models.PlayingContent
import com.bonborunote.niconicoviewer.notification.PLAYBACK_NOTIFICATION_CHANNEL_ID
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.exoplayer2.Player
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class BackgroundPlaybackService : LifecycleService(), KodeinAware {
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val binding by lazy {
    DataBindingUtil.inflate<LayoutServiceContainerBinding>(layoutInflater,
        R.layout.layout_service_container, null, false)
  }

  private val viewModel: BackgroundPlaybackViewModel by instance()
  private val layoutInflater: LayoutInflater by instance()
  private val windowManager: WindowManager by instance()
  private val notificationManager: NotificationManager by instance()

  override fun onCreate() {
    super.onCreate()
    lifecycle.addObserver(viewModel)
    viewModel.movieUrl.observe(this, Observer {
      viewModel.play()
    })
    viewModel.playerState.observe(this, Observer {
      when (it) {
        Player.STATE_ENDED -> stopSelf()
        null -> Unit
      }
    })
    startForeground(0x0001, createPlaybackNotification())
    attachContainer()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    intent?.let {
      Glide.with(this)
          .asBitmap()
          .load(intent.getStringExtra(EXTRA_THUMBNAIL))
          .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
              notificationManager.notify(0x0001, createPlaybackNotification(resource))
            }
          })
      when (it.action) {
        ACTION_REPLAY -> viewModel.replay()
        ACTION_PAUSE, ACTION_PLAY -> viewModel.togglePlay()
        ACTION_FORWARD -> viewModel.forward()
        ACTION_STOP -> viewModel.stop()
        else -> {
          val contentId = it.getStringExtra(EXTRA_CONTENT_ID)
          if (contentId.isNotBlank()) {
            viewModel.load(binding.container, contentId)
          }
        }
      }
    }
    return Service.START_NOT_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    stopForeground(true)
    viewModel.stop()
    viewModel.finalize(binding.container)
    removeContainer()
  }

  private fun attachContainer() {
    val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      TYPE_APPLICATION_OVERLAY
    } else {
      TYPE_SYSTEM_OVERLAY
    }
    val params = WindowManager.LayoutParams(0, 0, flag, FLAG_WATCH_OUTSIDE_TOUCH,
        PixelFormat.TRANSLUCENT)
    windowManager.addView(binding.root, params)
  }

  private fun removeContainer() {
    windowManager.removeView(binding.root)
  }

  private fun Context.createPlaybackNotification(picture: Bitmap? = null): Notification {
    return NotificationCompat.Builder(this, PLAYBACK_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon_background)
        .setContentTitle("バックグラウンド再生")
        .setContentText("hello")
        .addAction(
            NotificationCompat.Action.Builder(R.drawable.notification_icon_background, "戻す",
                createAction(ACTION_REPLAY)).build())
        .apply {
          if (viewModel.isPlaying()) {
            addAction(
                NotificationCompat.Action.Builder(R.drawable.notification_icon_background, "一時停止",
                    createAction(ACTION_PAUSE)).build())
          } else {
            addAction(
                NotificationCompat.Action.Builder(R.drawable.notification_icon_background, "再生",
                    createAction(ACTION_PLAY)).build())
          }
        }
        .addAction(
            NotificationCompat.Action.Builder(R.drawable.notification_icon_background, "進める",
                createAction(ACTION_FORWARD)).build())
        .addAction(
            NotificationCompat.Action.Builder(R.drawable.notification_icon_background, "停止",
                createAction(ACTION_STOP)).build())
        .setStyle(DecoratedMediaCustomViewStyle().setShowActionsInCompactView(1, 3))
        .apply {
          if (picture != null) {
            setLargeIcon(picture)
            setColorized(true)
            Palette.from(picture).generate().let {
              setColor(it.getLightVibrantColor(Color.CYAN))
            }
          }
        }
        .build()
  }

  private fun createAction(action: String): PendingIntent {
    return PendingIntent.getService(this, 0x0000,
        Intent(this, BackgroundPlaybackService::class.java).setAction(action),
        PendingIntent.FLAG_UPDATE_CURRENT)
  }

  companion object {
    const val EXTRA_CONTENT_ID = "content_id"
    const val EXTRA_THUMBNAIL = "thumbnail"

    const val ACTION_PLAY = "action_play"
    const val ACTION_PAUSE = "action_pause"
    const val ACTION_FORWARD = "action_forward"
    const val ACTION_REPLAY = "action_replay"
    const val ACTION_STOP = "action_stop"

    fun startService(context: Context, content: PlayingContent) {
      ContextCompat.startForegroundService(context,
          Intent(context, BackgroundPlaybackService::class.java).apply {
            putExtra(EXTRA_CONTENT_ID, content.contentId.value)
            putExtra(EXTRA_THUMBNAIL, content.thumbnail)
          })
    }

    fun stopService(context: Context) {
      context.stopService(Intent(context, BackgroundPlaybackService::class.java))
    }
  }
}
