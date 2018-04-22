package com.bonborunote.niconicoviewer.search.ui

import android.arch.paging.PositionalDataSource
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.search.domain.Content
import com.bonborunote.niconicoviewer.search.domain.ContentRepository
import com.bonborunote.niconicoviewer.search.domain.Sort.VIEW_COUNT_DESC

class SearchResultDataSource(
    private val keyword: String,
    private val contentRepository: ContentRepository,
    private val clickCallback: (content: Content) -> Unit
) : PositionalDataSource<SearchContentItem>() {

  override fun loadInitial(params: LoadInitialParams,
      callback: LoadInitialCallback<SearchContentItem>) {
    val result = contentRepository.search(
        keyword = keyword,
        sort = VIEW_COUNT_DESC,
        context = null,
        jsonFilters = null,
        offset = 0,
        limit = params.pageSize
    )
    callback.onResult(result.map { SearchContentItem(it, clickCallback) }, 0)
  }

  override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<SearchContentItem>) {
//    val result = nicoSearchApi.search(
//        keyword = keyword,
//        targets = listOf(TITLE),
//        sort = VIEW_COUNT_DESC,
//        fields = listOf(CONTENT_ID, Field.TITLE, TAG, LENGTH_SECONDS),
//        offset = params.startPosition,
//        limit = params.loadSize)
//    callback.onResult(result.map { SearchContentItem(it, clickCallback) })
  }
}
