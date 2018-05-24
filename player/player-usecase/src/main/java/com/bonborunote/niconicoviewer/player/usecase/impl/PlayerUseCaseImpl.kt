package com.bonborunote.niconicoviewer.player.usecase.impl

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.player.domain.MediaUrlRepository
import com.bonborunote.niconicoviewer.player.domain.NicoDataSource
import com.bonborunote.niconicoviewer.player.usecase.PlayerUseCase
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.EventLogger
import okhttp3.OkHttpClient

internal class PlayerUseCaseImpl(
    context: Context,
    okHttpClient: OkHttpClient,
    private val mediaUrlRepository: MediaUrlRepository,
    private val handler: Handler = Handler()
) : PlayerUseCase {
  private val mediaSourceFactory = ExtractorMediaSource.Factory(
      NicoDataSource.Factory(context, okHttpClient))
  private val renderersFactory = DefaultRenderersFactory(context)
  private val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(
      DefaultBandwidthMeter())
  private val trackSelector = DefaultTrackSelector(adaptiveTrackSelectionFactory)
  private val logger = EventLogger(trackSelector)
  private val player: ExoPlayer by lazy {
    ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector).apply {
      playWhenReady = true
    }
  }

  override fun findMediaUrl(contentId: String, container: ViewGroup,
      callback: (mediaUrl: String) -> Unit) {
    mediaUrlRepository.findMediaUrl(contentId, container, callback)
  }

  override fun bind(playerView: PlayerView) {
    playerView.player = player
  }

  override fun play(mediaUrl: String) {
    player.prepare(mediaSourceFactory.createMediaSource(Uri.parse(mediaUrl), handler, logger))
  }

  override fun stop(): Long {
    player.stop()
    return player.currentPosition
  }

  override fun togglePlay() {
    player.playWhenReady = !player.playWhenReady
  }

  override fun seekTo(positionMs: Long) {
    player.seekTo(positionMs)
  }

  override fun currentPosition(): Long {
    return player.currentPosition
  }

  override fun duration(): Long {
    return player.duration
  }
}

class PlayerUseCaseFactory {
  fun build(
      context: Context,
      okHttpClient: OkHttpClient,
      mediaUrlRepository: MediaUrlRepository,
      handler: Handler = Handler()
  ): PlayerUseCase {
    return PlayerUseCaseImpl(context, okHttpClient, mediaUrlRepository, handler)
  }
}
