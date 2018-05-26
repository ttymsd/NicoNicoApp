package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.search.domain.ContentRepository
import com.bonborunote.niconivoviewer.search.usecase.SearchUseCase
import com.bonborunote.niconivoviewer.search.usecase.impl.SearchUseCaseFactory
import java.util.concurrent.Executors

class SearchViewModel private constructor(
  private val searchUseCase: SearchUseCase
) : ViewModel(), LifecycleObserver {

  private val pagedConfig = PagedList.Config.Builder()
    .setEnablePlaceholders(false)
    .setPageSize(30)
    .setInitialLoadSizeHint(30)
    .build()
  val keyword = MutableLiveData<String>()
  private val dataSourceFactory = switchMap(keyword, {
    MutableLiveData<SearchResultDataSourceFactory>().apply {
      postValue(SearchResultDataSourceFactory(it, searchUseCase, itemClickCallback))
    }
  })
  private val dataSource = switchMap(dataSourceFactory, {
    it.dataSourceLiveData
  })
  val contents: LiveData<PagedList<SearchContentItem>> = switchMap(dataSourceFactory, {
    LivePagedListBuilder<Int, SearchContentItem>(it, pagedConfig)
      .setFetchExecutor(Executors.newFixedThreadPool(5))
      .build()
  })
  val loading = switchMap(dataSource, { it.networkState })
  val error = switchMap(dataSource, { it.error })
  val playableContent = MutableLiveData<Content>()
  private val itemClickCallback: (content: Content) -> Unit = {
    playableContent.postValue(it)
  }

  fun search(keyword: String) {
    this.keyword.postValue(keyword)
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
    private val contentRepository: ContentRepository
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return SearchViewModel(SearchUseCaseFactory(contentRepository).create()) as? T
        ?: throw IllegalArgumentException()
    }
  }
}
