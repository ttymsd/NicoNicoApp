package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconivoviewer.search.usecase.SearchUseCase

class SearchDataSourceFactory(
    private val keyword: String,
    private val searchUseCase: SearchUseCase,
    private val clickCallback: (content: Content) -> Unit
) : DataSource.Factory<Int, SearchContentItem>() {

  val dataSourceLiveData = MutableLiveData<SearchDataSource>()

  override fun create(): DataSource<Int, SearchContentItem> {
    val dataSource = SearchDataSource(keyword, searchUseCase, clickCallback)
    dataSourceLiveData.postValue(dataSource)
    return dataSource
  }
}
