package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PositionalDataSource
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.search.domain.Sort.VIEW_COUNT_DESC
import com.bonborunote.niconivoviewer.search.usecase.SearchUseCase

class SearchResultDataSource(
    private val keyword: String,
    private val searchUseCase: SearchUseCase,
    private val clickCallback: (content: Content) -> Unit
) : PositionalDataSource<SearchContentItem>() {

  val networkState = MutableLiveData<Boolean>()
  val error = MutableLiveData<NicoNicoException>()

  override fun loadInitial(params: LoadInitialParams,
      callback: LoadInitialCallback<SearchContentItem>) {
    networkState.postValue(true)
    val result = searchUseCase.search(
        keyword = keyword,
        sort = VIEW_COUNT_DESC,
        offset = 0,
        limit = params.pageSize
    )
    callback.onResult(result.map { SearchContentItem(it, clickCallback) }, 0)
    networkState.postValue(false)
  }

  override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<SearchContentItem>) {
    networkState.postValue(true)
    val result = searchUseCase.search(
        keyword = keyword,
        sort = VIEW_COUNT_DESC,
        offset = params.startPosition,
        limit = params.loadSize
    )
    callback.onResult(result.map { SearchContentItem(it, clickCallback) })
    networkState.postValue(false)
  }
}
