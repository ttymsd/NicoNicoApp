package com.bonborunote.niconicoviewer.components.background

import android.arch.lifecycle.Lifecycle.Event.ON_CREATE
import android.arch.lifecycle.Lifecycle.Event.ON_DESTROY
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.player.usecase.PlayerUseCase
import com.google.android.exoplayer2.Player
import kotlin.math.roundToLong

class BackgroundPlaybackViewModel(
    private val playbackUseCase: PlayerUseCase
) : LifecycleObserver {

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

  @OnLifecycleEvent(ON_CREATE)
  fun onStart() {
    playbackUseCase.addEventListener(playerEventListener)
  }

  @OnLifecycleEvent(ON_DESTROY)
  fun onStop() {
    playbackUseCase.removeEventListener(playerEventListener)
  }

  fun load(container: ViewGroup, contentId: String) {
    playbackUseCase.findMediaUrl(contentId, container) {
      movieUrl.postValue(it)
    }
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


  fun finalize(container: ViewGroup) {
    playbackUseCase.finalize(container)
  }

  companion object {
    const val MAX_PROGRESS = 1000
    private const val FORWARD_DURATION = 30_000L
    private const val REPLAY_DURATION = 10_000L
  }
}
