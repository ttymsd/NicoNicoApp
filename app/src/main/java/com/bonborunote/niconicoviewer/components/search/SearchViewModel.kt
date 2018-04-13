package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.widget.SearchView
import android.util.Log

class SearchViewModel private constructor() : ViewModel(), LifecycleObserver, SearchView.OnQueryTextListener {

  val keyword = MutableLiveData<String>()

  init {
    Log.d("AAA", "init")
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
    Log.d("AAA", "onCreate")
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
    Log.d("AAA", "onDestroy")
  }

  override fun onQueryTextSubmit(query: String?): Boolean {
    return true
  }

  override fun onQueryTextChange(newText: String?): Boolean {
    return false
  }

  @Suppress("UNCHECKED_CAST")
  class Factory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return SearchViewModel() as? T ?: throw IllegalArgumentException()
    }
  }
}
