package com.bonborunote.niconicoviewer.components.player

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
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.ads.AdsMediaSource.MediaSourceFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
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

class PlaybackFragment : Fragment(), KodeinAware, YoutubeLikeBehavior.OnBehaviorStateListener {
  // instance作った時点ではactivity = null なので getterをoverrideする
  override val kodeinContext: KodeinContext<*>
    get() = kcontext(activity)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(PlaybackFragment::contentId.name, "") ?: ""
  }

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
    ExoPlayerFactory.newSimpleInstance(renderer, trackSelector)
  }
  private var javascriptEngine: JavaScriptEngine? = null
  private val onPlayerStateChangedListener: OnPlayerStateChangedListener? by lazy {
    activity as? OnPlayerStateChangedListener
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mediaSourceFactory = ExtractorMediaSource.Factory(
        NicoDataSource.Factory(activity, okHttpClient))
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
          initializePlayer(it)
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()
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
    }
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
