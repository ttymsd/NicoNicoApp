package com.bonborunote.niconicoviewer.components.background

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.arch.lifecycle.LifecycleService
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.support.v4.media.app.NotificationCompat.MediaStyle
import android.support.v4.media.session.MediaSessionCompat
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutServiceContainerBinding
import com.bonborunote.niconicoviewer.notification.PLAYBACK_NOTIFICATION_CHANNEL_ID
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

  override fun onBind(intent: Intent?): IBinder? = null

  private val binding by lazy {
    DataBindingUtil.inflate<LayoutServiceContainerBinding>(layoutInflater,
        R.layout.layout_service_container, null, false)
  }

  private val viewModel: BackgroundPlaybackViewModel by instance()
  private val layoutInflater: LayoutInflater by instance()
  private val windowManager: WindowManager by instance()

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
    val session = MediaSessionCompat(this, "hoge")
    session.setCallback(object : MediaSessionCompat.Callback() {
      override fun onPlay() {
        super.onPlay()
      }

      override fun onStop() {
        super.onStop()
      }

      override fun onPause() {
        super.onPause()
      }
    })
    startForeground(0x0001, createPlaybackNotification(session.sessionToken))
    attachContainer()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    super.onStartCommand(intent, flags, startId)
    intent?.let {
      when (it.action) {
        "a" -> viewModel.replay()
        "b" -> viewModel.togglePlay()
        "c" -> viewModel.forward()
        else -> {
          val contentId = it.getStringExtra("contentId")
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

  private fun Context.createPlaybackNotification(
      mediaSession: MediaSessionCompat.Token,
      picture: Bitmap? = null
  ): Notification {
    return NotificationCompat.Builder(this, PLAYBACK_NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.notification_icon_background)
        .setContentTitle("バックグラウンド再生")
        .setContentText("hello")
        .addAction(R.drawable.notification_icon_background, "play", createAction("a"))
        .addAction(R.drawable.notification_icon_background, "play2", createAction("b"))
        .addAction(R.drawable.notification_icon_background, "play3", createAction("c"))
        .setStyle(MediaStyle().setMediaSession(mediaSession).setShowActionsInCompactView(0, 1))
        .apply {
          if (picture != null) {
          }
        }
        .build()
  }

  private fun createAction(action: String): PendingIntent {
    return PendingIntent.getBroadcast(this, 0x0000,
        Intent(this, BackgroundPlaybackService::class.java).setAction(action),
        PendingIntent.FLAG_UPDATE_CURRENT)
  }

  companion object {
    fun startService(context: Context, contentId: String) {
      ContextCompat.startForegroundService(context,
          Intent(context, BackgroundPlaybackService::class.java).apply {
            putExtra("contentId", contentId)
          })
    }

    fun stopService(context: Context) {
      context.stopService(Intent(context, BackgroundPlaybackService::class.java))
    }
  }
}
