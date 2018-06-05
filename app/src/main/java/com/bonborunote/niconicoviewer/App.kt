package com.bonborunote.niconicoviewer

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import android.content.Intent
import android.support.multidex.MultiDexApplication
import android.support.v4.content.ContextCompat
import android.util.Log
import com.bonborunote.niconicoviewer.components.background.BackgroundPlaybackService
import com.bonborunote.niconicoviewer.modules.applicationModule
import com.bonborunote.niconicoviewer.modules.detailModule
import com.bonborunote.niconicoviewer.modules.latestModule
import com.bonborunote.niconicoviewer.modules.mainModule
import com.bonborunote.niconicoviewer.modules.playbackModule
import com.bonborunote.niconicoviewer.modules.searchModule
import com.jakewharton.threetenabp.AndroidThreeTen
import io.fabric.sdk.android.services.common.BackgroundPriorityRunnable
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.instance

class App : MultiDexApplication(), KodeinAware, LifecycleObserver {
  override val kodein = Kodein.lazy {
    import(androidModule(this@App))
    import(applicationModule)
    import(mainModule)
    import(searchModule)
    import(playbackModule)
    import(detailModule)
    import(latestModule)
  }

  private val okHttpClient: OkHttpClient by instance()

  fun okHttpClient(): OkHttpClient = okHttpClient

  override fun onCreate() {
    super.onCreate()
    AndroidThreeTen.init(this)
    ProcessLifecycleOwner.get().lifecycle.addObserver(this)
  }

  override fun onTerminate() {
    super.onTerminate()
    ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
  }

  @OnLifecycleEvent(ON_START)
  fun onStart() {
    Log.d("AAA", "onStart")
    stopService(Intent(this, BackgroundPlaybackService::class.java))
  }

  @OnLifecycleEvent(ON_STOP)
  fun onStop() {
    Log.d("AAA", "onEnd")
    ContextCompat.startForegroundService(this, Intent(this, BackgroundPlaybackService::class.java))
  }
}
