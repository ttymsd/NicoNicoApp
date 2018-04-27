package com.bonborunote.niconicoviewer.detail.infra

import android.util.Log
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentDetailRepository
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonborunote.niconicoviewer.network.NicoNicoDetailApi
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.response.ContentDetailResponseXml

class ContentDetailRepositoryImpl(private val api: NicoNicoDetailApi) : ContentDetailRepository {
  override fun getDetail(contentId: ContentId): ContentDetail {
    val response = api.detail(contentId.value).execute()
    if (300 <= response.code()) {
      throw NicoNicoException(response.code(), "detail error", response.errorBody().toString())
    }
    val responseXml = response.body() ?: throw RuntimeException()
    Log.d("OkHttp", "start:${responseXml.status}")
    responseXml.thumbXml.tags.forEach {
      Log.d("OkHttp", it.value)
    }
    Log.d("OkHttp", "end")
    return responseXml.toEntity()
  }

  private fun ContentDetailResponseXml.toEntity(): ContentDetail {
    return ContentDetail(ContentId(""))
  }
}
