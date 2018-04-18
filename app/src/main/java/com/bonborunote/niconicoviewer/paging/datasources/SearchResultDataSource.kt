package com.bonborunote.niconicoviewer.paging.datasources

import android.arch.paging.PageKeyedDataSource
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.response.Content

class SearchResultDataSource(
    private val nicoSearchApi: NicoNicoSearchApi
) : PageKeyedDataSource<Int, Content>() {

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Content>) {
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Content>) {
  }

  override fun loadInitial(params: LoadInitialParams<Int>,
      callback: LoadInitialCallback<Int, Content>) {
  }
}
