package com.bonborunote.niconicoviewer.network

import com.bonborunote.niconicoviewer.network.response.VideosResponseXml
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RssApi {
  @GET("newarrival?rss=2.0")
  fun getLatestVideos(): Call<VideosResponseXml>

  @GET("user/{userId}/video?rss=2.0")
  fun getUserVideos(@Path("userId") userId: String): Call<VideosResponseXml>

  @GET("ch{channelId}/video?rss=2.0")
  fun getChannelVideos(@Path("channelId") channelId: String): Call<VideosResponseXml>

  companion object {
    const val BASE_URL = "http://www.nicovideo.jp/"
    const val CHANNEL_BASE_URL = "http://ch.nicovideo.jp/"
  }
}
