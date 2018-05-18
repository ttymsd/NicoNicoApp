package com.bonborunote.niconicoviewer

import android.support.multidex.MultiDexApplication
import com.bonborunote.niconicoviewer.modules.applicationModule
import com.bonborunote.niconicoviewer.modules.detailModule
import com.bonborunote.niconicoviewer.modules.latestModule
import com.bonborunote.niconicoviewer.modules.mainModule
import com.bonborunote.niconicoviewer.modules.playbackModule
import com.bonborunote.niconicoviewer.modules.searchModule
import com.jakewharton.threetenabp.AndroidThreeTen
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.androidModule
import org.kodein.di.generic.instance

class App : MultiDexApplication(), KodeinAware {
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
  }
}
