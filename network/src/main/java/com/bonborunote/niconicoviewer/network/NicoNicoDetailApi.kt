package com.bonborunote.niconicoviewer.network

import com.bonborunote.niconicoviewer.network.response.ContentDetailXml
import com.bonborunote.niconicoviewer.network.response.ContentDetailResponseXml
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

abstract class NicoNicoDetailApi(
    retrofit: Retrofit
) {
  protected val api = retrofit.create(Service::class.java)

  abstract fun detail(contentId: String): ContentDetailXml

  interface Service {
    @GET("/{id}")
    fun detail(@Path("id") contentId: String): Call<ContentDetailResponseXml>
  }

  companion object {
    const val BASE_URL = "http://ext.nicovideo.jp/api/getthumbinfo"
  }
}
