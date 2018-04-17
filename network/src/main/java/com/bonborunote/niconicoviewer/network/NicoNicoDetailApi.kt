package com.bonborunote.niconicoviewer.network

import com.bonborunote.niconicoviewer.network.response.ContentDetail
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

abstract class NicoNicoDetailApi(
    retrofit: Retrofit
) {
  protected val api = retrofit.create(Service::class.java)

  abstract fun detail(contentId: String): ContentDetail

  interface Service {
    @GET("")
    fun detail(@Query("id") contentId: String): Call<ContentDetail>
  }
}
