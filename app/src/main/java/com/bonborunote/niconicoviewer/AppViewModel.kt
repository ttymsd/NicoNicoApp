package com.bonborunote.niconicoviewer

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.components.background.BackgroundPlaybackService

class AppViewModel(private val context: Context) : LifecycleObserver {

  val playingContentId = MutableLiveData<String>()

  @OnLifecycleEvent(ON_START)
  fun onStart() {
    BackgroundPlaybackService.stopService(context)
  }

  @OnLifecycleEvent(ON_STOP)
  fun onStop() {
    playingContentId.value?.let {
      BackgroundPlaybackService.startService(context, it)
    }
  }

  fun startPlay(contentId: ContentId) {
    playingContentId.postValue(contentId.value)
  }

  fun stopPlay() {
    playingContentId.postValue(null)
  }
}
