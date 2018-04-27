package com.bonborunote.niconicoviewer.detail.infra

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
    return responseXml.toEntity()
  }

  private fun ContentDetailResponseXml.toEntity(): ContentDetail {
    return ContentDetail(ContentId(""))
  }
}
