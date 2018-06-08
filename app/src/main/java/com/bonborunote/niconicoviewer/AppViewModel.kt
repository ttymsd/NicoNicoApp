package com.bonborunote.niconicoviewer

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.bonborunote.niconicoviewer.components.background.BackgroundPlaybackService
import com.bonborunote.niconicoviewer.models.PlayingContent

class AppViewModel(private val context: Context) : LifecycleObserver {

  val playingContent = MutableLiveData<PlayingContent>()

  @OnLifecycleEvent(ON_START)
  fun onStart() {
    BackgroundPlaybackService.stopService(context)
  }

  @OnLifecycleEvent(ON_STOP)
  fun onStop() {
    playingContent.value?.let {
      BackgroundPlaybackService.startService(context, it)
    }
  }

  fun startPlay(content: PlayingContent) {
    playingContent.postValue(content)
  }

  fun stopPlay() {
    playingContent.postValue(null)
  }
}
