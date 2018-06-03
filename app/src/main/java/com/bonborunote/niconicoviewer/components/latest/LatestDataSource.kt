package com.bonborunote.niconicoviewer.components.latest

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCase
import com.bonborunote.niconicoviewer.network.NicoNicoException

class LatestDataSource(
  private val latestUseCase: LatestUseCase,
  private val clickCallback: (content: LatestVideo) -> Unit
) : PageKeyedDataSource<Int, LatestVideoItem>() {

  val networkState = MutableLiveData<Boolean>()
  val error = MutableLiveData<NicoNicoException>()

  override fun loadInitial(params: LoadInitialParams<Int>,
    callback: LoadInitialCallback<Int, LatestVideoItem>) {
    networkState.postValue(true)
    val result = latestUseCase.getLatest(1)
    callback.onResult(result.map { LatestVideoItem(it, clickCallback) }, 0, 2)
    networkState.postValue(false)
  }

  override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, LatestVideoItem>) {
    networkState.postValue(true)
    val result = latestUseCase.getLatest(params.key)
    callback.onResult(result.map { LatestVideoItem(it, clickCallback) }, params.key + 1)
    networkState.postValue(false)
  }

  override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, LatestVideoItem>) {
  }
}
