package com.bonborunote.niconicoviewer.components.playback

import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.MainThread
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.player.usecase.PlayerUseCase
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import jp.bglb.bonboru.behaviors.YoutubeLikeBehavior
import timber.log.Timber
import kotlin.math.roundToLong

class PlaybackViewModel(
    private val playbackUseCase: PlayerUseCase
) : ViewModel(), LifecycleObserver {
  val playerSizeState = MutableLiveData<Int>()
  val playerState = MutableLiveData<Int>()
  val movieUrl = MutableLiveData<String>()
  val seekPosition = MutableLiveData<Long>()
  val progress = MutableLiveData<Int>()
  val isPlaying = MutableLiveData<Boolean>()

  private val playerEventListener = object : Player.DefaultEventListener() {
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
      playerState.postValue(playbackState)
      isPlaying.postValue(playWhenReady)
    }
  }

  @OnLifecycleEvent(ON_START)
  fun onStart() {
    playbackUseCase.addEventListener(playerEventListener)
  }

  @OnLifecycleEvent(ON_STOP)
  fun onStop() {
    playbackUseCase.removeEventListener(playerEventListener)
  }

  @OnLifecycleEvent(ON_DESTROY)
  fun onDestroy() {
    initialize()
  }

  fun finalize(container: ViewGroup) {
    playbackUseCase.finalize(container)
  }

  fun findMediaUrl(container: ViewGroup, contentId: String) {
    playbackUseCase.findMediaUrl(contentId, container) {
      Timber.d("id:$contentId, $it")
      movieUrl.postValue(it)
    }
  }

  fun reload(contentId: String) {
    stop()
    initialize()
    playbackUseCase.reload(contentId) {
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

  fun seekTo(progress: Int) {
    val ms: Long = (progress / MAX_PROGRESS.toFloat() * playbackUseCase.duration()).roundToLong()
    playbackUseCase.seekTo(ms)
  }

  fun updateProgress() {
    val position = playbackUseCase.currentPosition()
    val duration = playbackUseCase.duration()
    progress.postValue((MAX_PROGRESS.toFloat() * position / duration).toInt())
  }

  fun togglePlay() {
    playbackUseCase.togglePlay()
  }

  fun forward() {
    playbackUseCase.seekTo(playbackUseCase.currentPosition() + FORWARD_DURATION)
  }

  fun replay() {
    playbackUseCase.seekTo(playbackUseCase.currentPosition() - REPLAY_DURATION)
  }

  @MainThread fun expand() {
    playerSizeState.value = YoutubeLikeBehavior.STATE_EXPANDED
  }

  @MainThread fun shrink() {
    playerSizeState.value = YoutubeLikeBehavior.STATE_SHRINK
  }

  private fun initialize() {
    playerSizeState.postValue(-1)
    playerState.postValue(-1)
    isPlaying.postValue(false)
    seekPosition.postValue(0)
    progress.postValue(0)
    movieUrl.postValue(null)
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
      private val useCase: PlayerUseCase
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return PlaybackViewModel(useCase) as? T ?: throw IllegalArgumentException()
    }
  }

  companion object {
    const val MAX_PROGRESS = 1000
    private const val FORWARD_DURATION = 30_000L
    private const val REPLAY_DURATION = 10_000L
  }
}
