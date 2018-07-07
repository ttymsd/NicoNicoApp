package com.bonborunote.niconicoviewer.components.playback

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.arch.lifecycle.Observer
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.graphics.drawable.Icon
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.OnDoubleTapListener
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.SeekBar
import com.bonborunote.niconicoviewer.AppViewModel
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.higherMashmallow
import com.bonborunote.niconicoviewer.common.higherOreo
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.databinding.FragmentPlaybackBinding
import com.google.android.exoplayer2.Player
import jp.bglb.bonboru.behaviors.YoutubeLikeBehavior
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class PlaybackFragment : Fragment(), KodeinAware, YoutubeLikeBehavior.OnBehaviorStateListener, SeekBar.OnSeekBarChangeListener {

  override val kodeinContext: KodeinContext<*>
    get() = kcontext(activity)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(PlaybackFragment::contentId.name, "") ?: ""
  }

  private val appViewModel: AppViewModel by instance()
  private val playbackViewModel: PlaybackViewModel by instance()

  private val onPlayerStateChangedListener: OnPlayerStateChangedListener? by lazy {
    activity as? OnPlayerStateChangedListener
  }

  private lateinit var binding: FragmentPlaybackBinding
  private var seekBarAnimator: ViewPropertyAnimator? = null
  private var receiver: BroadcastReceiver? = null
  private var playingObserver: Observer<Boolean>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(playbackViewModel)
    playbackViewModel.movieUrl.observe(this, Observer { url ->
      if (url == null) {
        seekBarAnimator?.cancel()
        binding.progress.visibility = View.VISIBLE
        binding.controller.alpha = 0f
      } else {
        binding.progress.visibility = View.GONE
        showSeekBar()
        playbackViewModel.bind(binding.playerView)
        playbackViewModel.play()
      }
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
    playbackViewModel.playerState.observe(this, Observer {
      when (it) {
        Player.STATE_ENDED -> binding.controller.animate().alpha(1f).start()
        Player.STATE_READY -> showSeekBar()
        else -> Unit
      }
    })
    playbackViewModel.isPlaying.observe(this, Observer { isPlaying ->
      isPlaying ?: return@Observer
      if (isPlaying) {
        binding.playPause.setImageResource(R.drawable.ic_pause_white)
      } else {
        binding.playPause.setImageResource(R.drawable.ic_play_arrow_white)
      }
    })
    playbackViewModel.playerSizeState.observe(this, Observer {
      when (it) {
        YoutubeLikeBehavior.STATE_EXPANDED -> binding.root.expand()
        YoutubeLikeBehavior.STATE_SHRINK -> binding.root.shrink()
        else -> Unit
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
    binding.seekBar.max = PlaybackViewModel.MAX_PROGRESS
    YoutubeLikeBehavior.from(binding.root)?.let {
      it.listener = this
      it.draggable = false
    }
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
    binding.seekBar.setOnSeekBarChangeListener(this)
    binding.playPause.setOnClickListener {
      playbackViewModel.togglePlay()
    }
    binding.forward.setOnClickListener {
      playbackViewModel.forward()
    }
    binding.replay.setOnClickListener {
      playbackViewModel.replay()
    }
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
    if (this::binding.isInitialized) {
      binding.controller.visibility = View.VISIBLE
    }
    receiver?.let {
      activity?.unregisterReceiver(it)
    }
    receiver = null
    playingObserver?.let {
      playbackViewModel.isPlaying.removeObserver(it)
    }
    playingObserver = null
    if (!higherMashmallow()) {
      playbackViewModel.play()
    }
  }

  override fun onPause() {
    super.onPause()
    val currentActivity = activity
    if (enablePipMode() && currentActivity != null) {
      startPipMode(currentActivity)
    }
    if (!higherMashmallow() || currentActivity == null) {
      playbackViewModel.stop()
    }
  }

  override fun onStop() {
    super.onStop()
    if (higherMashmallow()) {
      playbackViewModel.stop()
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    appViewModel.stopPlay()
    playbackViewModel.finalize(binding.dummyContainer)
  }

  override fun onBehaviorStateChanged(newState: Int) {
    if (newState == YoutubeLikeBehavior.STATE_TO_LEFT
      || newState == YoutubeLikeBehavior.STATE_TO_RIGHT) {
      onPlayerStateChangedListener?.remove()
    }
  }

  private fun enablePipMode(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
      && playbackViewModel.enablePIP()
      && !isRemoving
  }

  @RequiresApi(VERSION_CODES.O)
  private fun startPipMode(activity: FragmentActivity) {
    val params = PictureInPictureParams.Builder()
      .build()
    activity.enterPictureInPictureMode(params)
    updatePipAction(activity, true)
  }

  @RequiresApi(VERSION_CODES.O)
  private fun updatePipAction(activity: FragmentActivity, isPlay: Boolean) {
    val actions = arrayListOf<RemoteAction>()

    val replayIntent = PendingIntent.getBroadcast(activity,
      REQUEST_CODE_REPLAY,
      Intent(ACTION_REPLAY),
      0)
    val replayIcon = Icon.createWithResource(activity, R.drawable.baseline_replay_10_white_48)
    actions.add(RemoteAction(replayIcon,
      getString(R.string.player_action_back),
      getString(R.string.player_action_back),
      replayIntent))

    if (isPlay) {
      val pauseIntent = PendingIntent.getBroadcast(activity, REQUEST_CODE_PAUSE,
        Intent(ACTION_PAUSE),
        0)
      val pauseIcon = Icon.createWithResource(activity, R.drawable.ic_pause_white)
      actions.add(RemoteAction(pauseIcon,
        getString(R.string.player_action_pause),
        getString(R.string.player_action_pause),
        pauseIntent))
    } else {
      val playIntent = PendingIntent.getBroadcast(activity, REQUEST_CODE_PAUSE,
        Intent(ACTION_PAUSE),
        0)
      val playIcon = Icon.createWithResource(activity, R.drawable.ic_play_arrow_white)
      actions.add(RemoteAction(playIcon,
        getString(R.string.player_action_play),
        getString(R.string.player_action_play),
        playIntent))
    }

    val forwardIntent = PendingIntent.getBroadcast(activity, REQUEST_CODE_FORWARD,
      Intent(ACTION_FORWARD), 0)
    val forwardIcon = Icon.createWithResource(activity, R.drawable.baseline_forward_30_white_48)
    actions.add(RemoteAction(forwardIcon,
      getString(R.string.player_action_forward),
      getString(R.string.player_action_forward),
      forwardIntent))

    val params = PictureInPictureParams.Builder()
      .setActions(actions)
      .build()
    activity.setPictureInPictureParams(params)
  }

  override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean) {
    super.onPictureInPictureModeChanged(isInPictureInPictureMode)
    binding.controller.visibility = View.GONE
    playingObserver = Observer { isPlaying ->
      isPlaying ?: return@Observer
      activity?.let { activity ->
        if (higherOreo()) {
          updatePipAction(activity, isPlaying)
        }
      }
    }
    playingObserver?.let {
      playbackViewModel.isPlaying.observeForever(it)
    }
    receiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return
        when (intent.action) {
          ACTION_REPLAY -> playbackViewModel.replay()
          ACTION_FORWARD -> playbackViewModel.forward()
          ACTION_PAUSE -> playbackViewModel.togglePlay()
          ACTION_START -> playbackViewModel.togglePlay()
        }
      }
    }.apply {
      activity?.registerReceiver(this, IntentFilter().apply {
        addAction(ACTION_REPLAY)
        addAction(ACTION_FORWARD)
        addAction(ACTION_PAUSE)
        addAction(ACTION_START)
      })
    }
  }

  override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
    if (fromUser) {
      playbackViewModel.seekTo(progress)
    }
  }

  override fun onStartTrackingTouch(seekBar: SeekBar) {
    seekBarAnimator?.cancel()
    binding.seekBar.alpha = 1f
  }

  override fun onStopTrackingTouch(seekBar: SeekBar) {
    showSeekBar()
  }

  private fun acceptDraggable() {
    YoutubeLikeBehavior.from(binding.root)?.draggable = true
  }

  private fun ignoreDrag() {
    YoutubeLikeBehavior.from(binding.root)?.draggable = false
  }

  private fun showSeekBar() {
    seekBarAnimator?.cancel()

    seekBarAnimator = binding.controller.animate().apply {
      duration = SHOW_ANIMATION_DURATION
      interpolator = AccelerateDecelerateInterpolator()
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
    seekBarAnimator = binding.controller.animate().apply {
      duration = DISMISS_ANIMATION_DURATION
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

  private fun View.expand() {
    YoutubeLikeBehavior.from(this)?.updateState(YoutubeLikeBehavior.STATE_EXPANDED)
  }

  private fun View.shrink() {
    YoutubeLikeBehavior.from(this)?.updateState(YoutubeLikeBehavior.STATE_SHRINK)
  }

  interface OnPlayerStateChangedListener {
    fun remove()
  }

  companion object {
    const val TAG = "PlaybackFragment"
    private val SHOW_ANIMATION_DURATION = 1_000L
    private val DISMISS_ANIMATION_DURATION = 6_000L

    const val ACTION_REPLAY = "action_replay"
    const val ACTION_START = "action_start"
    const val ACTION_PAUSE = "action_pause"
    const val ACTION_FORWARD = "action_forward"

    const val REQUEST_CODE_REPLAY = 0x0000
    const val REQUEST_CODE_START = 0x0001
    const val REQUEST_CODE_PAUSE = 0x0002
    const val REQUEST_CODE_FORWARD = 0x0003

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
