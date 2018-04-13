package com.bonborunote.niconicoviewer.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val applicationModule = Kodein.Module {
  bind<Gson>() with singleton { GsonBuilder().create() }
  bind<HttpLoggingInterceptor>("log") with singleton {
    HttpLoggingInterceptor().apply {
      level = BODY
    }
  }
  bind<OkHttpClient>() with singleton {
    OkHttpClient.Builder()
        .addNetworkInterceptor(instance("log"))
        .build()
  }
  bind<Retrofit>() with singleton {
    Retrofit.Builder()
        .baseUrl("http://api.search.nicovideo.jp")
        .addConverterFactory(GsonConverterFactory.create(instance()))
        .client(instance())
        .build()
  }
}
