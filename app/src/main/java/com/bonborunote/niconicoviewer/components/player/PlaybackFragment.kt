package com.bonborunote.niconicoviewer.components.player

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.FragmentPlaybackBinding
import com.bonborunote.niconicoviewer.player.NicoDataSource
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.EventLogger
import jp.bglb.bonboru.behaviors.YoutubeLikeBehavior
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext
import timber.log.Timber

class PlaybackFragment : Fragment(), KodeinAware, Player.EventListener, YoutubeLikeBehavior.OnBehaviorStateListener {
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(PlaybackFragment::contentId.name, "") ?: ""
  }

  private val playbackViewModel: PlaybackViewModel by instance()
  private val okHttpClient: OkHttpClient by instance()
  private val handler = Handler()
  private val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(
      DefaultBandwidthMeter())
  private val trackSelector = DefaultTrackSelector(adaptiveTrackSelectionFactory)
  private val logger = EventLogger(trackSelector)
  private lateinit var binding: FragmentPlaybackBinding
  private lateinit var mediaSourceFactory: MediaSourceFactory
  private val player: ExoPlayer by lazy {
    val renderer = DefaultRenderersFactory(activity)
    ExoPlayerFactory.newSimpleInstance(renderer, trackSelector).apply {
      addListener(this@PlaybackFragment)
    }
  }
  private var javascriptEngine: JavaScriptEngine? = null
  private val onPlayerStateChangedListener: OnPlayerStateChangedListener? by lazy {
    activity as? OnPlayerStateChangedListener
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mediaSourceFactory = ExtractorMediaSource.Factory(
        NicoDataSource.Factory(activity, okHttpClient))
    playbackViewModel.movieUrl.observe(this, Observer {
      it?.let {
        initializePlayer(it)
      }
    })
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playback, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    YoutubeLikeBehavior.from(binding.root)?.listener = this
    activity?.let {
      javascriptEngine = JavaScriptEngine(it)
      binding.dummyContainer.addView(javascriptEngine)
      javascriptEngine?.apply {
        findMediaUrl(contentId) {
          Timber.d("id:$contentId, $it")
          playbackViewModel.movieUrl.postValue(it)
        }
      }
    }
  }

  override fun onStart() {
    super.onStart()
    playbackViewModel.movieUrl.value?.let {
      initializePlayer(it)
    }
  }

  override fun onStop() {
    super.onStop()
    playbackViewModel.seekPosition.postValue(player.currentPosition)
    player.stop()
  }

  override fun onBehaviorStateChanged(newState: Long) {
    if (newState == YoutubeLikeBehavior.STATE_TO_LEFT
        || newState == YoutubeLikeBehavior.STATE_TO_RIGHT) {
      player.stop()
      onPlayerStateChangedListener?.remove()
    }
  }

  private fun initializePlayer(url: String) {
    activity?.runOnUiThread {
      binding.playerView.player = player
      player.prepare(mediaSourceFactory.createMediaSource(Uri.parse(url), handler, logger))
      player.seekTo(playbackViewModel.seekPosition.value ?: 0)
    }
  }

  override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
  }

  override fun onSeekProcessed() {
  }

  override fun onTracksChanged(trackGroups: TrackGroupArray?,
      trackSelections: TrackSelectionArray?) {
  }

  override fun onPlayerError(error: ExoPlaybackException?) {
  }

  override fun onLoadingChanged(isLoading: Boolean) {
  }

  override fun onPositionDiscontinuity(reason: Int) {
  }

  override fun onRepeatModeChanged(repeatMode: Int) {
  }

  override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
  }

  override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
  }

  override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
  }

  interface OnPlayerStateChangedListener {
    fun remove()
  }

  companion object {
    const val TAG = "PlaybackFragment"

    fun newInstance(contentId: String): Fragment = PlaybackFragment().apply {
      arguments = Bundle().apply {
        putString(PlaybackFragment::contentId.name, contentId)
      }
    }
  }
}
