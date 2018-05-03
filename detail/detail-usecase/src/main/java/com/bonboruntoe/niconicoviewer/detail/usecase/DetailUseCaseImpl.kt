package com.bonboruntoe.niconicoviewer.detail.usecase

import com.bonborunote.niconicoviewer.detail.domain.ChannelId
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentDetailRepository
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonborunote.niconicoviewer.detail.domain.OwnerId
import com.bonborunote.niconicoviewer.detail.domain.RelationVideo

internal class DetailUseCaseImpl(
  private val detailRepository: ContentDetailRepository
) : DetailUseCase {
  override fun getDetail(id: ContentId): ContentDetail {
    return detailRepository.getDetail(id)
  }

  override fun getUserVideos(userId: OwnerId): List<RelationVideo> {
    return detailRepository.getUserVideos(userId)
  }

  override fun getChannelVideos(channelId: ChannelId): List<RelationVideo> {
    return detailRepository.getChannelVideos(channelId)
  }
}

class DetailUseCaseFactory {
  fun create(detailRepository: ContentDetailRepository): DetailUseCase {
    return DetailUseCaseImpl(detailRepository)
  }
}
