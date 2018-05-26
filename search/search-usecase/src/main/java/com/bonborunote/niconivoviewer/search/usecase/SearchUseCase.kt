package com.bonborunote.niconivoviewer.search.usecase

import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.search.domain.Sort

interface SearchUseCase {
  fun search(keyword: String, offset: Int, limit: Int, sort: Sort): List<Content>

  fun searchFromTag(tag: String, offset: Int, limit: Int, sort: Sort): List<Content>
}
