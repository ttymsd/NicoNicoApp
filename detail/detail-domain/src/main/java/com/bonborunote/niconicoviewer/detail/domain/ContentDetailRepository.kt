package com.bonborunote.niconicoviewer.detail.domain

interface ContentDetailRepository {
  fun getDetail(contentId: ContentId): ContentDetail

  fun getUserVideos(ownerId: OwnerId): List<RelationVideo>

  fun getChannelVideos(channelId: ChannelId): List<RelationVideo>
}
