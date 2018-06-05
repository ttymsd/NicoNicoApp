package com.bonborunote.niconicoviewer.components.background

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bonborunote.niconicoviewer.notification.createPlaybackNotification
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class BackgroundPlaybackService : Service(), KodeinAware {
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  override fun onBind(intent: Intent?): IBinder? = null

  private val viewModel: BackgroundPlaybackViewModel by instance()

  override fun onCreate() {
    super.onCreate()
    startForeground(0x0001, createPlaybackNotification())
    viewModel.onStart()
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onDestroy() {
    super.onDestroy()
    stopForeground(true)
    viewModel.onStop()
  }
}
