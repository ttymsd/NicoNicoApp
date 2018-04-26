package com.bonborunote.niconicoviewer.player.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.player.domain.MediaUrlRepository
import com.bonborunote.niconicoviewer.player.infra.MediaUrlRepositoryFactory
import timber.log.Timber

class PlaybackViewModel(
    private val mediaUrlRepository: MediaUrlRepository
) : ViewModel() {
  val movieUrl = MutableLiveData<String>()
  val seekPosition = MutableLiveData<Long>()

  fun findMediaUrl(container: ViewGroup, contentId: String) {
    mediaUrlRepository.findMediaUrl(contentId, container) {
      Timber.d("id:$contentId, $it")
      movieUrl.postValue(it)
    }
  }

  @Suppress("UNCHECKED_CAST")
  class Factory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return PlaybackViewModel(MediaUrlRepositoryFactory().create()) as? T
          ?: throw IllegalArgumentException()
    }
  }
}
