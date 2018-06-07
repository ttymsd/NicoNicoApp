package com.bonborunote.niconicoviewer.modules

import com.bonborunote.niconicoviewer.AppViewModel
import com.bonborunote.niconicoviewer.network.NicoNicoDetailApi
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.RssApi
import com.bonborunote.niconicoviewer.player.domain.CookieJar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okhttp3.logging.HttpLoggingInterceptor.Level.HEADERS
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val applicationModule = Kodein.Module {
  bind<ExecutorService>() with singleton { Executors.newFixedThreadPool(5) }
  bind<okhttp3.CookieJar>() with singleton { CookieJar() }
  bind<Gson>() with singleton { GsonBuilder().create() }
  bind<HttpLoggingInterceptor>("log") with singleton {
    HttpLoggingInterceptor().apply {
      level = HEADERS
    }
  }
  bind<OkHttpClient>() with singleton {
    OkHttpClient.Builder()
      .addNetworkInterceptor(instance("log"))
      .cookieJar(instance())
      .build()
  }
  bind<Retrofit>("search") with singleton {
    Retrofit.Builder()
      .baseUrl(NicoNicoSearchApi.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create(instance()))
      .client(instance())
      .build()
  }
  bind<NicoNicoSearchApi>() with singleton {
    val retrofit: Retrofit = instance("search")
    retrofit.create(NicoNicoSearchApi::class.java)
  }
  bind<Retrofit>("detail") with singleton {
    Retrofit.Builder()
      .baseUrl(NicoNicoDetailApi.BASE_URL)
      .addConverterFactory(SimpleXmlConverterFactory.create())
      .client(instance())
      .build()
  }
  bind<NicoNicoDetailApi>() with singleton {
    val retrofit: Retrofit = instance("detail")
    retrofit.create(NicoNicoDetailApi::class.java)
  }
  bind<Retrofit>("rss") with singleton {
    Retrofit.Builder()
      .baseUrl(RssApi.BASE_URL)
      .addConverterFactory(SimpleXmlConverterFactory.create())
      .client(instance())
      .build()
  }
  bind<Retrofit>("channel_rss") with singleton {
    Retrofit.Builder()
      .baseUrl(RssApi.CHANNEL_BASE_URL)
      .addConverterFactory(SimpleXmlConverterFactory.create())
      .client(instance())
      .build()
  }
  bind<RssApi>("video_rss") with singleton {
    val retrofit: Retrofit = instance("rss")
    retrofit.create(RssApi::class.java)
  }
  bind<RssApi>("channel_rss") with singleton {
    val retrofit: Retrofit = instance("channel_rss")
    retrofit.create(RssApi::class.java)
  }
}
