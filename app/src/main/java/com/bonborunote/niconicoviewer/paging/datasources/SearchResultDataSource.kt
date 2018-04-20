package com.bonborunote.niconicoviewer.paging.datasources

import android.arch.paging.PositionalDataSource
import com.bonborunote.niconicoviewer.components.search.SearchContentItem
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.CONTENT_ID
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.LENGTH_SECONDS
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.TAG
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Sort.VIEW_COUNT_DESC
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Target.TITLE

class SearchResultDataSource(
    private val nicoSearchApi: NicoNicoSearchApi
) : PositionalDataSource<SearchContentItem>() {

  override fun loadInitial(params: LoadInitialParams,
      callback: LoadInitialCallback<SearchContentItem>) {
    val result = nicoSearchApi.search(
        keyword = "シャドバ",
        targets = listOf(TITLE),
        sort = VIEW_COUNT_DESC,
        fields = listOf(CONTENT_ID, Field.TITLE, TAG, LENGTH_SECONDS),
        offset = 0,
        limit = params.pageSize)
    callback.onResult(result.map { SearchContentItem(it, {}) }, 0)
  }

  override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<SearchContentItem>) {
    val result = nicoSearchApi.search(
        keyword = "シャドバ",
        targets = listOf(TITLE),
        sort = VIEW_COUNT_DESC,
        fields = listOf(CONTENT_ID, Field.TITLE, TAG, LENGTH_SECONDS),
        offset = params.startPosition,
        limit = params.loadSize)
    callback.onResult(result.map { SearchContentItem(it, {}) })
  }
}
