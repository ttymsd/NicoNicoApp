package com.bonborunote.niconicoviewer.detail.domain

interface ContentDetailRepository {
  fun getDetail(contentId: ContentId): ContentDetail
}
