package com.bonborunote.niconicoviewer.paging.datasources

import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.components.search.SearchContentItem
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi

class SearchResultDataSourceFactory(
    private val nicoSearchApi: NicoNicoSearchApi
) : DataSource.Factory<Int, SearchContentItem>() {
  override fun create(): DataSource<Int, SearchContentItem> {
    return SearchResultDataSource(nicoSearchApi)
  }
}
