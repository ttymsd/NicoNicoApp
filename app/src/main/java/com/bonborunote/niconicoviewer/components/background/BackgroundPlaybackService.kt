package com.bonborunote.niconicoviewer.components.background

import android.app.Service
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutServiceContainerBinding
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

  private val binding by lazy {
    DataBindingUtil.inflate<LayoutServiceContainerBinding>(layoutInflater,
        R.layout.layout_service_container, null, false)
  }

  private val viewModel: BackgroundPlaybackViewModel by instance()
  private val layoutInflater: LayoutInflater by instance()
  private val windowManager: WindowManager by instance()

  override fun onCreate() {
    super.onCreate()
    startForeground(0x0001, createPlaybackNotification())
    viewModel.onStart()
    attachContainer()
    viewModel.load(binding.container, "sm33044418")
    Handler().postDelayed({ stopSelf() }, 5000)
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return super.onStartCommand(intent, flags, startId)
  }

  override fun onDestroy() {
    super.onDestroy()
    stopForeground(true)
    viewModel.finalize(binding.container)
    viewModel.onStop()
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

  companion object {
    fun startService(contentId: String) {

    }
  }
}
