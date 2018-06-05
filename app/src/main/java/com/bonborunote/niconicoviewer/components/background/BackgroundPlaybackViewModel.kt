package com.bonborunote.niconicoviewer.components.background

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.bonborunote.niconicoviewer.player.usecase.PlayerUseCase
import com.google.android.exoplayer2.Player

class BackgroundPlaybackViewModel(
    private val playbackUseCase: PlayerUseCase
) {

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

  fun onStart() {
    playbackUseCase.addEventListener(playerEventListener)
    Log.d("AAA", "${playbackUseCase}")
    playbackUseCase.togglePlay()
  }

  fun onStop() {
    playbackUseCase.removeEventListener(playerEventListener)
    playbackUseCase.stop()
  }
}
