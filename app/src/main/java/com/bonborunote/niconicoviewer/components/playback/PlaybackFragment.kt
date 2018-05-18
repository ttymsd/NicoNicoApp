package com.bonborunote.niconicoviewer.components.playback

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.higherMashmallow
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.databinding.FragmentPlaybackBinding
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class PlaybackFragment : Fragment(), KodeinAware {
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(PlaybackFragment::contentId.name, "") ?: ""
  }

  private val playbackViewModel: PlaybackViewModel by instance()

  private val onPlayerStateChangedListener: OnPlayerStateChangedListener? by lazy {
    activity as? OnPlayerStateChangedListener
  }
  private lateinit var binding: FragmentPlaybackBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    playbackViewModel.movieUrl.observe(this, Observer {
      playbackViewModel.bind(binding.playerView)
      playbackViewModel.play()
      binding.root.parent.requestLayout()
    })
    playbackViewModel.seekPosition.observe(this, Observer {
      it?.let {
        playbackViewModel.seekTo(it)
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
    playbackViewModel.findMediaUrl(binding.dummyContainer, contentId)
  }

  override fun onStart() {
    super.onStart()
    if (higherMashmallow()) {
      playbackViewModel.play()
    }
  }

  override fun onResume() {
    super.onResume()
    if (!higherMashmallow()) {
      playbackViewModel.play()
    }
  }

  override fun onPause() {
    super.onPause()
    if (!higherMashmallow()) {
      playbackViewModel.stop()
    }
  }

  override fun onStop() {
    super.onStop()
    if (higherMashmallow()) {
      playbackViewModel.stop()
    }
  }

  interface OnPlayerStateChangedListener {
    fun remove()
  }

  companion object {
    const val TAG = "PlaybackFragment"

    fun newInstance(contentId: ContentId): Fragment = PlaybackFragment().apply {
      arguments = Bundle().apply {
        putString(PlaybackFragment::contentId.name, contentId.value)
      }
    }
  }
}
