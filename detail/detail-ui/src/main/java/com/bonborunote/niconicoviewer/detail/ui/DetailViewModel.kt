package com.bonborunote.niconicoviewer.detail.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.util.Log
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonborunote.niconicoviewer.detail.domain.RelationVideo
import com.bonboruntoe.niconicoviewer.detail.usecase.DetailUseCase
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class DetailViewModel(
  private val detailUseCase: DetailUseCase
) : ViewModel() {

  val detail = MutableLiveData<ContentDetail>()
  val usersVideos = MutableLiveData<List<RelationVideo>>()
  val channelVideos = MutableLiveData<List<RelationVideo>>()

  fun load(contentId: ContentId) {
    async(CommonPool) {
      try {
        val detailResponse = detailUseCase.getDetail(contentId)
        detail.postValue(detailResponse)
        detailResponse.owner?.let {
          usersVideos.postValue(detailUseCase.getUserVideos(it.id))
        }
        detailResponse.channel?.let {
          val videos = detailUseCase.getChannelVideos(it.id)
          Log.d("OkHttp", "${videos.size}")
          channelVideos.postValue(videos)
        }
      } catch (exeption: Exception) {
        Log.d("OkHttp", exeption.message, exeption)
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
