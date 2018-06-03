package com.bonborunote.niconicoviewer.detail.domain

import com.bonborunote.niconicoviewer.common.models.ChannelId
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.OwnerId
import com.bonborunote.niconicoviewer.common.models.RelationVideo

interface ContentDetailRepository {
  fun getDetail(contentId: ContentId): ContentDetail

  fun getUserVideos(ownerId: OwnerId): List<RelationVideo>

  fun getChannelVideos(channelId: ChannelId): List<RelationVideo>
}
