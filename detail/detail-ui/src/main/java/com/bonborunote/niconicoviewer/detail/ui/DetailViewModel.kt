package com.bonborunote.niconicoviewer.detail.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonboruntoe.niconicoviewer.detail.usecase.DetailUseCase
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class DetailViewModel(
  private val detailUseCase: DetailUseCase
) : ViewModel() {

  val detail = MutableLiveData<ContentDetail>()

  fun load(contentId: ContentId) {
    async(CommonPool) {
      try {
        detail.postValue(detailUseCase.getDetail(contentId))
      } catch (exeption: Exception) {
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
    private val detailUseCase: DetailUseCase
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return DetailViewModel(detailUseCase)as? T
        ?: throw IllegalArgumentException()
    }
  }
}
