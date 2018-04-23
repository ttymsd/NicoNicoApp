package com.bonborunote.niconivoviewer.search.usecase.impl

import com.bonborunote.niconicoviewer.search.domain.Content
import com.bonborunote.niconicoviewer.search.domain.ContentRepository
import com.bonborunote.niconicoviewer.search.domain.Sort
import com.bonborunote.niconivoviewer.search.usecase.SearchUseCase

internal class SearchUseCaseImpl(
    private val contentRepository: ContentRepository
) : SearchUseCase {
  override fun search(keyword: String, offset: Int, limit: Int, sort: Sort): List<Content> {
    return contentRepository.search(keyword, sort, offset, limit)
  }
}

class SearchUseCaseFactory(
    private val contentRepository: ContentRepository
) {
  fun create(): SearchUseCase {
    return SearchUseCaseImpl(contentRepository)
  }
}
