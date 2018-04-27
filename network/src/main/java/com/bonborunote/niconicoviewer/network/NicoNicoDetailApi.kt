package com.bonborunote.niconicoviewer.network

import com.bonborunote.niconicoviewer.network.response.ContentDetailResponseXml
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NicoNicoDetailApi {
  @GET("{id}")
  fun detail(@Path("id") contentId: String): Call<ContentDetailResponseXml>

  companion object {
    const val BASE_URL = "http://ext.nicovideo.jp/api/getthumbinfo/"
  }
}
