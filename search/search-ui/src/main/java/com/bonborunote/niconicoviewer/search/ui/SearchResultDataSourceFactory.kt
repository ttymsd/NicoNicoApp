package com.bonborunote.niconicoviewer.search.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconivoviewer.search.usecase.SearchUseCase

class SearchResultDataSourceFactory(
    private val keyword: String,
    private val searchUseCase: SearchUseCase,
    private val clickCallback: (content: Content) -> Unit
) : DataSource.Factory<Int, SearchContentItem>() {

  val dataSourceLiveData = MutableLiveData<SearchResultDataSource>()

  override fun create(): DataSource<Int, SearchContentItem> {
    val dataSource = SearchResultDataSource(keyword, searchUseCase, clickCallback)
    dataSourceLiveData.postValue(dataSource)
    return dataSource
  }
}
