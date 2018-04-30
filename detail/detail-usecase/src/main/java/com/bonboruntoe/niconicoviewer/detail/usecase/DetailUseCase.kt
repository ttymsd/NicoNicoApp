package com.bonboruntoe.niconicoviewer.detail.usecase

import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentId

interface DetailUseCase {
  fun getDetail(id: ContentId): ContentDetail
}
