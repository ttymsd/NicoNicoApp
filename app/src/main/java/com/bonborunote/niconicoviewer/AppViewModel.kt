package com.bonborunote.niconicoviewer

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import com.bonborunote.niconicoviewer.components.background.BackgroundPlaybackService
import android.arch.lifecycle.OnLifecycleEvent

class AppViewModel(private val context: Context): LifecycleObserver {

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
}
