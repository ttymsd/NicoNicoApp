package com.bonborunote.niconicoviewer.components.playback

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.GestureDetector
import android.view.GestureDetector.OnDoubleTapListener
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.higherMashmallow
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.databinding.FragmentPlaybackBinding
import jp.bglb.bonboru.behaviors.YoutubeLikeBehavior
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class PlaybackFragment : Fragment(), KodeinAware, YoutubeLikeBehavior.OnBehaviorStateListener {
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
  private var seekBarAnimator: ViewPropertyAnimator? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    playbackViewModel.movieUrl.observe(this, Observer {
      showSeekBar()
      playbackViewModel.bind(binding.playerView)
      playbackViewModel.play()
    })
    playbackViewModel.seekPosition.observe(this, Observer {
      it?.let {
        playbackViewModel.seekTo(it)
      }
    })
    playbackViewModel.progress.observe(this, Observer {
      it?.let {
        binding.seekBar.progress = it
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
    binding.seekBar.max = 1000
    YoutubeLikeBehavior.from(binding.root)?.let {
      it.listener = this
      it.draggable = false
    }
    playbackViewModel.findMediaUrl(binding.dummyContainer, contentId)
    val detector = GestureDetector(activity, SimpleOnGestureListener())
    detector.setOnDoubleTapListener(object : OnDoubleTapListener {
      override fun onDoubleTap(event: MotionEvent): Boolean {
        return false
      }

      override fun onDoubleTapEvent(event: MotionEvent): Boolean {
        return false
      }

      override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        showSeekBar()
        return true
      }
    })
    binding.root.setOnTouchListener { _, motionEvent ->
      detector.onTouchEvent(motionEvent)
      true
    }
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

  override fun onBehaviorStateChanged(newState: Int) {
    if (newState == YoutubeLikeBehavior.STATE_TO_LEFT
        || newState == YoutubeLikeBehavior.STATE_TO_RIGHT) {
      onPlayerStateChangedListener?.remove()
    }
  }

  private fun acceptDraggable() {
    YoutubeLikeBehavior.from(binding.root)?.draggable = true
  }

  private fun ignoreDrag() {
    YoutubeLikeBehavior.from(binding.root)?.draggable = false
  }

  private fun showSeekBar() {
    seekBarAnimator?.cancel()

    seekBarAnimator = binding.seekBar.animate().apply {
      startDelay = 0
      duration = 1_000L
      alphaBy(0f)
      alpha(1f)
      setUpdateListener {
        playbackViewModel.updateProgress()
      }
      setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
          ignoreDrag()
        }

        override fun onAnimationEnd(animation: Animator?) {
          dissMissSeekBar()
        }
      })
      start()
    }
  }

  private fun dissMissSeekBar() {
    seekBarAnimator = binding.seekBar.animate().apply {
      duration = 6_000L
      alphaBy(1f)
      alpha(0f)
      setInterpolator {
        if (it <= 0.8f) 0f else 1f - (1f - it) / 0.2f
      }
      setUpdateListener {
        playbackViewModel.updateProgress()
      }
      setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationCancel(animation: Animator?) {
          binding.seekBar.alpha = 1f
        }

        override fun onAnimationEnd(animation: Animator?) {
          acceptDraggable()
        }
      })
      start()
    }
  }

  interface OnPlayerStateChangedListener {
    fun remove()
  }

  companion object {
    const val TAG = "PlaybackFragment"

    fun createArgs(contentId: ContentId): Bundle {
      return Bundle().apply {
        putString(PlaybackFragment::contentId.name, contentId.value)
      }
    }

    fun newInstance(contentId: ContentId): Fragment = PlaybackFragment().apply {
      arguments = createArgs(contentId)
    }
  }
}
