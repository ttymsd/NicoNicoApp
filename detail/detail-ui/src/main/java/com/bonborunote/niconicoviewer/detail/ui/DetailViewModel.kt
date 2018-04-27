package com.bonborunote.niconicoviewer.detail.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
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
      repository.getDetail(contentId)
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
