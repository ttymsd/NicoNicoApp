package com.bonborunote.niconicoviewer.components.latest

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.latest.domain.LatestVideoRepository
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCase
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCaseFactory
import java.util.concurrent.ExecutorService

class LatestViewModel private constructor(
  private val executor: ExecutorService,
  private val latestUsecase: LatestUseCase
) : ViewModel(), LifecycleObserver {

  val loading = MutableLiveData<Boolean>()
  private val pagedConfig = PagedList.Config.Builder()
    .setEnablePlaceholders(false)
    .setPageSize(30)
    .setInitialLoadSizeHint(30)
    .build()
  private val clickCallback: (LatestVideo) -> Unit = {
    l?.invoke(it)
  }
  private val dataSourceFactory = MutableLiveData<LatestDataSourceFactory>()
  val videos = switchMap(dataSourceFactory, {
    LivePagedListBuilder<Int, LatestVideoItem>(it, pagedConfig)
      .setFetchExecutor(executor)
      .build()
  })
  private var l: ((LatestVideo) -> Unit)? = null

  fun load(l: ((LatestVideo) -> Unit)?) {
    this.l = l
    dataSourceFactory.postValue(LatestDataSourceFactory(latestUsecase, clickCallback))
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
    private val executor: ExecutorService,
    private val latestVideoRepository: LatestVideoRepository
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return LatestViewModel(
        executor,
        LatestUseCaseFactory().build(latestVideoRepository)) as? T
        ?: throw IllegalArgumentException()
    }
  }
}
