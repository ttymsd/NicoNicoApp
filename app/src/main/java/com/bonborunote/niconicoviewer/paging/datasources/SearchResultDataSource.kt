package com.bonborunote.niconicoviewer.paging.datasources

import android.arch.paging.PageKeyedDataSource
import android.util.Log
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
) : PageKeyedDataSource<Int, SearchContentItem>() {
  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, SearchContentItem>) {
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, SearchContentItem>) {
    Log.d("OkHttp", "more")
    val result = nicoSearchApi.search(
        keyword = "シャドバ",
        targets = listOf(TITLE),
        sort = VIEW_COUNT_DESC,
        fields = listOf(CONTENT_ID, Field.TITLE, TAG, LENGTH_SECONDS),
        offset = params.key,
        limit = params.requestedLoadSize)
    callback.onResult(result.map { SearchContentItem(it, {}) }, params.key + params.requestedLoadSize)
  }

  override fun loadInitial(params: LoadInitialParams<Int>,
      callback: LoadInitialCallback<Int, SearchContentItem>) {
    Log.d("OkHttp", "initialize")
    val result = nicoSearchApi.search(
        keyword = "シャドバ",
        targets = listOf(TITLE),
        sort = VIEW_COUNT_DESC,
        fields = listOf(CONTENT_ID, Field.TITLE, TAG, LENGTH_SECONDS),
        offset = 0,
        limit = params.requestedLoadSize)
    callback.onResult(result.map { SearchContentItem(it, {}) }, 0, params.requestedLoadSize)
  }
}
