package com.bonboruntoe.niconicoviewer.detail.usecase

import com.bonborunote.niconicoviewer.common.models.ChannelId
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.OwnerId
import com.bonborunote.niconicoviewer.common.models.RelationVideo

interface DetailUseCase {
  fun getDetail(id: ContentId): ContentDetail

  fun getUserVideos(userId: OwnerId): List<RelationVideo>

  fun getChannelVideos(channelId: ChannelId): List<RelationVideo>
}
