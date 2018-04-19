package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import android.support.v7.widget.SearchView
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.response.Content
import com.bonborunote.niconicoviewer.paging.datasources.SearchResultDataSourceFactory
import com.bonborunote.niconicoviewer.repositories.SearchResultRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import java.util.concurrent.Executors

class SearchViewModel private constructor(
    private val repository: SearchResultRepository,
    private val api: NicoNicoSearchApi
) : ViewModel(), LifecycleObserver, SearchView.OnQueryTextListener {

  private val pagedConfig = PagedList.Config.Builder()
      .setEnablePlaceholders(false)
      .setPageSize(30)
      .setInitialLoadSizeHint(30)
      .build()
  val keyword = MutableLiveData<String>()
  val contents: LiveData<PagedList<SearchContentItem>> = switchMap(keyword, {
    LivePagedListBuilder<Int, SearchContentItem>(SearchResultDataSourceFactory(api), pagedConfig)
        .setFetchExecutor(Executors.newFixedThreadPool(5))
        .build()
  })
  val loading = MutableLiveData<Boolean>()
  val error = MutableLiveData<NicoNicoException>()
  val playableContent = MutableLiveData<Content>()

  private var subscription = Disposables.disposed()
  private val itemClickCallback: (content: Content) -> Unit = {
    playableContent.postValue(it)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
    subscription = CompositeDisposable(
        repository.error
            .subscribe {
              error.postValue(it)
            },
        repository.loading
            .subscribe {
              loading.postValue(it)
            }
    )
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    subscription.dispose()
  }

  override fun onQueryTextSubmit(query: String?): Boolean {
    query?.let {
      keyword.postValue(it)
    }
    return true
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    return false
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
      private val repository: SearchResultRepository
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return SearchViewModel(repository,
          repository.nicoNicoSearchApi) as? T ?: throw IllegalArgumentException()
    }
  }
}
