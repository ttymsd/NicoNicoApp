package com.bonborunote.niconicoviewer.components.player

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class PlaybackViewModel : ViewModel() {
  val movieUrl = MutableLiveData<String>()
  @Suppress("UNCHECKED_CAST")
  class Factory(
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return PlaybackViewModel() as? T ?: throw IllegalArgumentException()
    }
  }
}
