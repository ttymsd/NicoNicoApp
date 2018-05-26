package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconivoviewer.search.usecase.SearchUseCase

class TagSearchDataSourceFactory(
  private val tag: String,
  private val searchUseCase: SearchUseCase,
  private val clickCallback: (content: Content) -> Unit
) : DataSource.Factory<Int, SearchContentItem>() {

  val dataSourceLiveData = MutableLiveData<TagSearchDataSource>()

  override fun create(): DataSource<Int, SearchContentItem> {
    val dataSource = TagSearchDataSource(tag, searchUseCase, clickCallback)
    dataSourceLiveData.postValue(dataSource)
    return dataSource
  }
}