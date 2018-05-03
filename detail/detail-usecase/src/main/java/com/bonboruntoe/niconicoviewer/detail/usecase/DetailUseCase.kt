package com.bonboruntoe.niconicoviewer.detail.usecase

import com.bonborunote.niconicoviewer.detail.domain.ChannelId
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonborunote.niconicoviewer.detail.domain.OwnerId
import com.bonborunote.niconicoviewer.detail.domain.RelationVideo

interface DetailUseCase {
  fun getDetail(id: ContentId): ContentDetail

  fun getUserVideos(userId: OwnerId): List<RelationVideo>

  fun getChannelVideos(channelId: ChannelId): List<RelationVideo>
}
