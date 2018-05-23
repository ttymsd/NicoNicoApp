package com.bonborunote.niconicoviewer.components.playback

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.player.infra.MediaUrlRepositoryFactory
import com.bonborunote.niconicoviewer.player.usecase.PlayerUseCase
import com.bonborunote.niconicoviewer.player.usecase.impl.PlayerUseCaseFactory
import com.google.android.exoplayer2.ui.PlayerView
import okhttp3.OkHttpClient
import timber.log.Timber

class PlaybackViewModel(
    private val playbackUseCase: PlayerUseCase
) : ViewModel() {
  val movieUrl = MutableLiveData<String>()
  val seekPosition = MutableLiveData<Long>()
  val progress = MutableLiveData<Int>()

  fun findMediaUrl(container: ViewGroup, contentId: String) {
    playbackUseCase.findMediaUrl(contentId, container) {
      Timber.d("id:$contentId, $it")
      movieUrl.postValue(it)
    }
  }

  fun bind(playerView: PlayerView) {
    playbackUseCase.bind(playerView)
  }

  fun play() {
    movieUrl.value?.let {
      playbackUseCase.play(it)
      playbackUseCase.seekTo(seekPosition.value ?: 0)
    }
  }

  fun stop() {
    playbackUseCase.stop()
    seekPosition.postValue(playbackUseCase.currentPosition())
  }

  fun seekTo(positionMs: Long) {
    playbackUseCase.seekTo(positionMs)
  }

  fun updateProgress() {
    val position = playbackUseCase.currentPosition()
    val duration = playbackUseCase.duration()
    progress.postValue((1000 * position / duration).toInt())
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
      private val context: Context,
      private val okHttpClient: OkHttpClient
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      val useCase = PlayerUseCaseFactory().build(
          context,
          okHttpClient,
          MediaUrlRepositoryFactory().create())
      return PlaybackViewModel(useCase) as? T ?: throw IllegalArgumentException()
    }
  }
}
