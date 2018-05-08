package com.bonborunote.niconicoviewer.latest.ui

import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.latest.domain.LatestVideoRepository
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCase
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCaseFactory
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class LatestViewModel private constructor(
    private val latestUsecase: LatestUseCase
) : ViewModel(), LifecycleObserver {

  val loading = MutableLiveData<Boolean>()
  val videos = MutableLiveData<List<LatestVideo>>()

  fun load() {
    async(CommonPool) {
      try {
        loading.postValue(true)
        videos.postValue(latestUsecase.getLatest())
      } catch (exception: Exception) {
      } finally {
        loading.postValue(false)
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
      private val latestVideoRepository: LatestVideoRepository
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return LatestViewModel(LatestUseCaseFactory().build(latestVideoRepository)) as? T
          ?: throw IllegalArgumentException()
    }
  }
}
