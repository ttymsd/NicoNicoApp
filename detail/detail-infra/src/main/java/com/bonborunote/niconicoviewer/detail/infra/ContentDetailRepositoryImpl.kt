package com.bonborunote.niconicoviewer.detail.infra

import android.net.Uri
import com.bonborunote.niconicoviewer.common.models.Channel
import com.bonborunote.niconicoviewer.common.models.ChannelId
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentDetailRepository
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.Owner
import com.bonborunote.niconicoviewer.common.models.OwnerId
import com.bonborunote.niconicoviewer.common.models.RelationVideo
import com.bonborunote.niconicoviewer.common.models.Tag
import com.bonborunote.niconicoviewer.common.models.fromDescription
import com.bonborunote.niconicoviewer.network.NicoNicoDetailApi
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.RssApi
import com.bonborunote.niconicoviewer.network.response.ItemXml
import com.bonborunote.niconicoviewer.network.response.ThumbXml

class ContentDetailRepositoryImpl(
  private val detailApi: NicoNicoDetailApi,
  private val rssApi: RssApi,
  private val channelRssApi: RssApi
) : ContentDetailRepository {
  override fun getDetail(contentId: ContentId): ContentDetail {
    val response = detailApi.detail(contentId.value).execute()
    if (300 <= response.code()) {
      throw NicoNicoException(response.code(), "detail error", response.errorBody().toString())
    }
    val xmlResponse = response?.body() ?: throw RuntimeException()
    if (xmlResponse.status != RESULT_OK) throw RuntimeException()
    return xmlResponse.thumbXml?.toEntity() ?: throw RuntimeException()
  }

  override fun getUserVideos(ownerId: OwnerId): List<RelationVideo> {
    val response = rssApi.getUserVideos(ownerId.value).execute()
    if (300 <= response.code()) {
      throw NicoNicoException(response.code(), "detail error", response.errorBody().toString())
    }
    val xmlResponse = response?.body() ?: throw RuntimeException()
    return xmlResponse.channel?.items?.map { it.toRelationVideo() } ?: emptyList()
  }

  override fun getChannelVideos(channelId: ChannelId): List<RelationVideo> {
    val response = channelRssApi.getChannelVideos(channelId.value).execute()
    if (300 <= response.code()) {
      throw NicoNicoException(response.code(), "detail error", response.errorBody().toString())
    }
    val xmlResponse = response?.body() ?: throw RuntimeException()
    return xmlResponse.channel?.items?.map { it.toRelationVideo() } ?: emptyList()
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

  private fun ItemXml.toRelationVideo(): RelationVideo {
    return RelationVideo(
      id = ContentId(Uri.parse(link).lastPathSegment),
      title = title,
      thumb = description.fromDescription()
    )
  }

  companion object {
    const val RESULT_OK = "ok"
  }
}
