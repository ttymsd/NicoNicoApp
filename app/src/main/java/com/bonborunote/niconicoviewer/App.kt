package com.bonborunote.niconicoviewer

import android.arch.lifecycle.ProcessLifecycleOwner
import android.support.multidex.MultiDexApplication
import com.bonborunote.niconicoviewer.modules.applicationModule
import com.bonborunote.niconicoviewer.modules.detailModule
import com.bonborunote.niconicoviewer.modules.latestModule
import com.bonborunote.niconicoviewer.modules.mainModule
import com.bonborunote.niconicoviewer.modules.playbackModule
import com.bonborunote.niconicoviewer.modules.preferenceModule
import com.bonborunote.niconicoviewer.modules.searchModule
import com.bonborunote.niconicoviewer.notification.createPlaybackChannel
import com.jakewharton.threetenabp.AndroidThreeTen
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance

open class App : MultiDexApplication(), KodeinAware {
  override val kodein = Kodein.lazy {
    val preference = Preference(this@App)
    bind<Preference>() with instance(preference)
    bind<AppViewModel>() with instance(AppViewModel(this@App, preference))
    import(androidModule(this@App))
    import(applicationModule)
    import(mainModule)
    import(searchModule)
    import(playbackModule)
    import(detailModule)
    import(latestModule)
    import(preferenceModule)
  }

  private val okHttpClient: OkHttpClient by instance()
  private val appViewModel: AppViewModel by instance()

  fun okHttpClient(): OkHttpClient = okHttpClient

  override fun onCreate() {
    super.onCreate()
    createPlaybackChannel()
    AndroidThreeTen.init(this)
    ProcessLifecycleOwner.get().lifecycle.addObserver(appViewModel)
  }

  override fun onTerminate() {
    super.onTerminate()
    ProcessLifecycleOwner.get().lifecycle.removeObserver(appViewModel)
  }
}
