package com.bonborunote.niconicoviewer.components.detail

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.RelationVideo
import com.bonborunote.niconicoviewer.models.LoadStatus
import com.bonborunote.niconicoviewer.models.LoadStatus.LOADED
import com.bonborunote.niconicoviewer.models.LoadStatus.LOADING
import com.bonboruntoe.niconicoviewer.detail.usecase.DetailUseCase
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import timber.log.Timber

class DetailViewModel(
    private val detailUseCase: DetailUseCase
) : ViewModel() {

  val loadState = MutableLiveData<LoadStatus>()
  val detail = MutableLiveData<ContentDetail>()
  val usersVideos = MutableLiveData<List<RelationVideo>>()
  val channelVideos = MutableLiveData<List<RelationVideo>>()

  fun load(contentId: ContentId) {
    getDetail(contentId)
  }

  fun reload(contentId: String) {
    getDetail(ContentId(contentId))
  }

  private fun getDetail(contentId: ContentId) {
    async(CommonPool) {
      try {
        loadState.postValue(LOADING)
        val detailResponse = detailUseCase.getDetail(contentId)
        detail.postValue(detailResponse)
        detailResponse.owner?.let {
          usersVideos.postValue(detailUseCase.getUserVideos(it.id))
        }
        detailResponse.channel?.let {
          val videos = detailUseCase.getChannelVideos(it.id)
          channelVideos.postValue(videos)
        }
      } catch (e: Exception) {
        Timber.e(e, e.message)
      } finally {
        loadState.postValue(LOADED)
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
