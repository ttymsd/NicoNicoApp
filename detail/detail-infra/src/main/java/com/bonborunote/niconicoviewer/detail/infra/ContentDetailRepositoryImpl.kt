package com.bonborunote.niconicoviewer.detail.infra

import com.bonborunote.niconicoviewer.detail.domain.Channel
import com.bonborunote.niconicoviewer.detail.domain.ChannelId
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentDetailRepository
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonborunote.niconicoviewer.detail.domain.Owner
import com.bonborunote.niconicoviewer.detail.domain.OwnerId
import com.bonborunote.niconicoviewer.detail.domain.Tag
import com.bonborunote.niconicoviewer.network.NicoNicoDetailApi
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.response.ThumbXml

class ContentDetailRepositoryImpl(private val api: NicoNicoDetailApi) : ContentDetailRepository {
  override fun getDetail(contentId: ContentId): ContentDetail {
    val response = api.detail(contentId.value).execute()
    if (300 <= response.code()) {
      throw NicoNicoException(response.code(), "detail error", response.errorBody().toString())
    }
    val xmlResponse = response?.body() ?: throw RuntimeException()
    if (xmlResponse.status != RESULT_OK) throw RuntimeException()
    return xmlResponse.thumbXml?.toEntity() ?: throw RuntimeException()
  }

  private fun ThumbXml.toEntity(): ContentDetail {
    return ContentDetail(
      id = ContentId(videoId),
      title = title,
      description = description,
      thumbnailUrl = thumbnailUrl,
      length = length,
      viewCount = viewCounter,
      commentCount = commentNum,
      myListCount = myListCounter,
      isLive = noLivePlay == 0,
      tags = tags.map { Tag(it.value) },
      owner = getOwner(),
      channel = getChannel()
    )
  }

  private fun ThumbXml.getOwner(): Owner? {
    val id = userId?.let { OwnerId(it) } ?: return null
    val name = userNickname ?: ""
    val thumb = userIconUrl ?: ""
    return Owner(id, name, thumb)
  }

  private fun ThumbXml.getChannel(): Channel? {
    val id = channelId?.let { ChannelId(it) } ?: return null
    val name = channelName ?: ""
    val thumb = channelIconUrl ?: ""
    return Channel(id, name, thumb)
  }

  companion object {
    const val RESULT_OK = "ok"
  }
}
