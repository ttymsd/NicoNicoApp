package com.bonborunote.niconicoviewer.components

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.widget.SearchView
import com.bonborunote.niconicoviewer.common.models.ContentId

class MainViewModel private constructor(
) : ViewModel(), LifecycleObserver, SearchView.OnQueryTextListener {

  val playableContent = MutableLiveData<ContentId>()
  val keyword = MutableLiveData<String>()

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
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
