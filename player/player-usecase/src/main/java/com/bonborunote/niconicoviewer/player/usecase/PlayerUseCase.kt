package com.bonborunote.niconicoviewer.player.usecase

import android.support.annotation.MainThread
import android.view.ViewGroup
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView

interface PlayerUseCase {

  @MainThread
  fun findMediaUrl(contentId: String, container: ViewGroup, callback: (mediaUrl: String) -> Unit)

  @MainThread
  fun reload(contentId: String, callback: (mediaUrl: String) -> Unit)

  @MainThread
  fun bind(playerView: PlayerView)

  @MainThread
  fun finalize(viewGroup: ViewGroup)

  fun play(mediaUrl: String)
  fun stop(): Long
  fun seekTo(positionMs: Long)
  fun currentPosition(): Long
  fun duration(): Long
  fun togglePlay()
  fun addEventListener(listener: Player.EventListener)
  fun removeEventListener(listener: Player.EventListener)
}
