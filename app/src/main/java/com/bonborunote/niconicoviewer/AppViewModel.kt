package com.bonborunote.niconicoviewer

import android.arch.lifecycle.Lifecycle.Event.ON_START
import android.arch.lifecycle.Lifecycle.Event.ON_STOP
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import com.bonborunote.niconicoviewer.components.background.BackgroundPlaybackService
import com.bonborunote.niconicoviewer.models.PlayingContent
import com.bonborunote.niconicoviewer.utils.checkOverlayPermission

class AppViewModel(private val context: Context,
  private val preference: Preference
) : LifecycleObserver {

  private var playingContent: PlayingContent? = null

  @OnLifecycleEvent(ON_START)
  fun onStart() {
    BackgroundPlaybackService.stopService(context)
  }

  @OnLifecycleEvent(ON_STOP)
  fun onStop() {
    playingContent?.let {
      if (context.checkOverlayPermission()
        && preference.backgroundPlaybackEnable()
        && !preference.pictureInPictureEnable()) {
        BackgroundPlaybackService.startService(context, it)
      }
    }
  }

  fun startPlay(content: PlayingContent) {
    playingContent = content
  }

  fun stopPlay() {
    playingContent = null
  }
}
