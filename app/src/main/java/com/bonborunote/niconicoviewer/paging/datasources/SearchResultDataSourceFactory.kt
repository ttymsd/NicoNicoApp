package com.bonborunote.niconicoviewer.paging.datasources

import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.response.Content

class SearchResultDataSourceFactory(
    private val nicoSearchApi: NicoNicoSearchApi
) : DataSource.Factory<Int, Content>() {
  override fun create(): DataSource<Int, Content> {
    return SearchResultDataSource(nicoSearchApi)
  }
}
