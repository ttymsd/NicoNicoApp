package com.bonborunote.niconicoviewer.paging.datasources

import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.components.search.SearchContentItem
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.response.Content

class SearchResultDataSourceFactory(
    private val keyword: String,
    private val nicoSearchApi: NicoNicoSearchApi,
    private val clickCallback: (content: Content) -> Unit
) : DataSource.Factory<Int, SearchContentItem>() {
  override fun create(): DataSource<Int, SearchContentItem> {
    return SearchResultDataSource(keyword, nicoSearchApi, clickCallback)
  }
}
