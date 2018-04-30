package com.bonboruntoe.niconicoviewer.detail.usecase

import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentDetailRepository
import com.bonborunote.niconicoviewer.detail.domain.ContentId

internal class DetailUseCaseImpl(
  private val detailRepository: ContentDetailRepository
) : DetailUseCase {
  override fun getDetail(id: ContentId): ContentDetail {
    return detailRepository.getDetail(id)
  }
}

class DetailUseCaseFactory {
  fun create(detailRepository: ContentDetailRepository): DetailUseCase {
    return DetailUseCaseImpl(detailRepository)
  }
}
