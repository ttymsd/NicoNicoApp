package com.bonborunote.niconicoviewer.components.player

import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.FragmentPlaybackBinding
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class PlaybackFragment : Fragment(), KodeinAware {
  // instance作った時点ではactivity = null なので getterをoverrideする
  override val kodeinContext: KodeinContext<*>
    get() = kcontext(activity)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(PlaybackFragment::contentId.name, "") ?: ""
  }

  private val okHttpClient: OkHttpClient by instance()
  private lateinit var binding: FragmentPlaybackBinding
  private lateinit var dataSourceFactory: DataSource.Factory
  private val player: ExoPlayer by lazy {
    val adaptiveTrackSelectionFactory = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())
    val trackSelector = DefaultTrackSelector(adaptiveTrackSelectionFactory)
    val renderer = DefaultRenderersFactory(activity)
    ExoPlayerFactory.newSimpleInstance(renderer, trackSelector)
  }
  private var javascriptEngine: JavaScriptEngine? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    dataSourceFactory = OkHttpDataSourceFactory(okHttpClient,
        Util.getUserAgent(activity, "ExoPlayer"), null)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_playback, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    activity?.let {
      javascriptEngine = JavaScriptEngine(it)
      binding.dummyContainer.addView(javascriptEngine)
      javascriptEngine?.apply {
        findMediaUrl(contentId) {
          Log.d("AAA", "id:$contentId, $it")
          initializePlayer(it)
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()
    player.stop()
  }

  private fun initializePlayer(url: String) {
    activity?.runOnUiThread {
      binding.playerView.player = player
      player.prepare(ExtractorMediaSource.Factory(dataSourceFactory)
          .createMediaSource(Uri.parse(url)))
    }
  }

  companion object {
    fun newInstance(contentId: String): Fragment = PlaybackFragment().apply {
      arguments = Bundle().apply {
        putString(PlaybackFragment::contentId.name, contentId)
      }
    }
  }
}
