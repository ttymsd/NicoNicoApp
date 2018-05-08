package com.bonborunote.niconicoviewer.components

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.models.Navigation
import com.bonborunote.niconicoviewer.models.Navigation.LATEST
import com.bonborunote.niconicoviewer.models.Navigation.SEARCH

class MainViewModel private constructor() : ViewModel(), LifecycleObserver {

  val currentPage = MutableLiveData<Navigation>()
  val playableContent = MutableLiveData<ContentId>()

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
  }

  fun navigateToLatest() {
    currentPage.postValue(LATEST)
  }

  fun navigateToSearch() {
    currentPage.postValue(SEARCH)
  }

  fun play(id: ContentId) {
    playableContent.postValue(id)
  }

  @Suppress("UNCHECKED_CAST")
  class Factory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return MainViewModel() as? T ?: throw IllegalArgumentException()
    }
  }
}
