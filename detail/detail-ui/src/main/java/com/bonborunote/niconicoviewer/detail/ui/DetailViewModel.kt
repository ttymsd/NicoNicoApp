package com.bonborunote.niconicoviewer.detail.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.bonborunote.niconicoviewer.detail.domain.ContentDetailRepository
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonborunote.niconicoviewer.detail.infra.ContentDetailRepositoryImpl
import com.bonborunote.niconicoviewer.network.NicoNicoDetailApi
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class DetailViewModel(
    private val repository: ContentDetailRepository
) : ViewModel() {

  fun detail(contentId: ContentId) {
    async(CommonPool) {
      try {
        repository.getDetail(contentId)
      } catch (e: Exception) {
        Log.d("OkHttp", e.message, e)
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
      private val api: NicoNicoDetailApi
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return DetailViewModel(ContentDetailRepositoryImpl(api))as? T
          ?: throw IllegalArgumentException()
    }
  }
}
