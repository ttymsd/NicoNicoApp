package com.bonborunote.niconicoviewer.components

import android.arch.lifecycle.Observer
import android.os.Build
import android.os.Bundle
import android.support.transition.Slide
import android.support.v7.app.AppCompatActivity
import android.view.Gravity.BOTTOM
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.common.models.RelationVideo
import com.bonborunote.niconicoviewer.components.detail.DetailFragment
import com.bonborunote.niconicoviewer.components.detail.DetailFragment.DetailClickListener
import com.bonborunote.niconicoviewer.components.detail.DetailViewModel
import com.bonborunote.niconicoviewer.components.latest.LatestVideosFragment
import com.bonborunote.niconicoviewer.components.playback.PlaybackFragment
import com.bonborunote.niconicoviewer.components.playback.PlaybackFragment.OnPlayerStateChangedListener
import com.bonborunote.niconicoviewer.components.playback.PlaybackViewModel
import com.bonborunote.niconicoviewer.components.search.SearchContainer
import com.bonborunote.niconicoviewer.components.search.SearchContainerArgs
import com.bonborunote.niconicoviewer.databinding.ActivityMainBinding
import com.bonborunote.niconicoviewer.utils.lazyBinding
import org.kodein.di.Copy.All
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class MainActivity : AppCompatActivity(),
    KodeinAware,
    OnPlayerStateChangedListener,
    LatestVideosFragment.LatestListItemClickListener,
    SearchContainer.SearchListItemClickListener,
    DetailClickListener {

  private val _parentKodein by closestKodein()
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by retainedKodein {
    extend(_parentKodein, copy = All)
  }

  private val binding by lazyBinding<ActivityMainBinding>(R.layout.activity_main)
  private val mainViewModel: MainViewModel by instance()
  private val playbackViewModel: PlaybackViewModel by instance()
  private val detailViewModel: DetailViewModel by instance()
  private val contentObserver = Observer<ContentId> {
    it ?: return@Observer
    reloadIfAttached(it)
  }
  private val keywordObserver = Observer<String> {
    val args = SearchContainerArgs.Builder().apply {
      this.keyword = it
    }.build().toBundle()
    Navigation.findNavController(this@MainActivity, R.id.my_nav_host_fragment)
        .navigate(R.id.search, args)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(mainViewModel)
    binding.mainViewModel = mainViewModel
    binding.setLifecycleOwner(this)
    binding.executePendingBindings()
    mainViewModel.playableContent.observe(this, contentObserver)
    mainViewModel.keyword.observe(this, keywordObserver)
    val host: NavHostFragment = supportFragmentManager
        .findFragmentById(R.id.my_nav_host_fragment) as? NavHostFragment ?: return
    NavigationUI.setupWithNavController(binding.navigation, host.navController)
    host.navController.addOnNavigatedListener { _, destination ->
      when (destination.id) {
        R.id.search -> {
          if (supportFragmentManager.findFragmentByTag(PlaybackFragment.TAG) != null) {
            playbackViewModel.shrink()
          }
        }
        else -> Unit
      }
    }
  }

  override fun onPause() {
    super.onPause()
    if (enablePipMode()) {
      enterPictureInPictureMode()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(mainViewModel)
  }

  override fun onBackPressed() {
    if (attachedPlaybackFragment()) {
      removePlaybackFragment()
    } else {
      super.onBackPressed()
    }
  }

  override fun remove() {
    if (attachedPlaybackFragment()) {
      removePlaybackFragment()
    }
  }

  override fun clickLatestItem(video: LatestVideo) {
    mainViewModel.play(video.id)
  }

  override fun clickSearchItem(video: Content) {
    mainViewModel.play(video.id)
  }

  override fun onReleationClicked(item: RelationVideo) {
    reloadIfAttached(item.id)
  }

  private fun enablePipMode(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && attachedPlaybackFragment()
  }

  private fun reloadIfAttached(contentId: ContentId) {
    if (supportFragmentManager.findFragmentByTag(PlaybackFragment.TAG) == null) {
      addPlaybackFragment(contentId)
    } else {
      playbackViewModel.reload(contentId.value)
      detailViewModel.reload(contentId.value)
    }
  }

  private fun addPlaybackFragment(id: ContentId) {
    supportFragmentManager.beginTransaction()
        .add(R.id.coordinator, PlaybackFragment.newInstance(id).apply {
          enterTransition = Slide().apply {
            slideEdge = BOTTOM
          }
        }, PlaybackFragment.TAG)
        .add(R.id.coordinator, DetailFragment.newInstance(id).apply {
          enterTransition = Slide().apply {
            slideEdge = BOTTOM
          }
        }, DetailFragment.TAG)
        .commit()
  }

  private fun attachedPlaybackFragment(): Boolean {
    return supportFragmentManager.findFragmentByTag(PlaybackFragment.TAG) != null
  }

  private fun removePlaybackFragment() {
    supportFragmentManager.beginTransaction()
        .apply {
          supportFragmentManager.findFragmentByTag(PlaybackFragment.TAG)?.let {
            remove(it)
          }
          supportFragmentManager.findFragmentByTag(DetailFragment.TAG)?.let {
            remove(it)
          }
        }
        .commit()
  }
}
