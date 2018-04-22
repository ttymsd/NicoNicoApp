package com.bonborunote.niconicoviewer.search.ui

import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.search.domain.Content
import com.bonborunote.niconicoviewer.search.domain.ContentRepository

class SearchResultDataSourceFactory(
    private val keyword: String,
    private val contentRepository: ContentRepository,
    private val clickCallback: (content: Content) -> Unit
) : DataSource.Factory<Int, SearchContentItem>() {
  override fun create(): DataSource<Int, SearchContentItem> {
    return SearchResultDataSource(keyword, contentRepository, clickCallback)
  }
}
