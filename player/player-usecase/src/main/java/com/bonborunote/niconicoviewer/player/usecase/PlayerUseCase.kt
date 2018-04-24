package com.bonborunote.niconicoviewer.player.usecase

interface PlayerUseCase {
  fun load(contentId: String)
  fun play()
  fun pause()
  fun stop()
  fun seekTo()
}
