package com.bonborunote.niconicoviewer.components

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.transition.Slide
import android.support.v7.app.AppCompatActivity
import android.view.Gravity.BOTTOM
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.components.detail.DetailFragment
import com.bonborunote.niconicoviewer.components.latest.LatestVideosFragment
import com.bonborunote.niconicoviewer.components.playback.PlaybackFragment
import com.bonborunote.niconicoviewer.components.playback.PlaybackFragment.OnPlayerStateChangedListener
import com.bonborunote.niconicoviewer.components.search.SearchContainer
import com.bonborunote.niconicoviewer.components.search.SearchViewModel
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

class MainActivity : AppCompatActivity(), KodeinAware, OnPlayerStateChangedListener, LatestVideosFragment.LatestListItemClickListener, SearchContainer.SearchListItemClickListener {
  private val _parentKodein by closestKodein()
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by retainedKodein {
    extend(_parentKodein, copy = All)
  }

  private val binding by lazyBinding<ActivityMainBinding>(R.layout.activity_main)
  private val mainViewModel: MainViewModel by instance()
  private val searchViewModel: SearchViewModel by instance()
  private val contentObserver = Observer<ContentId> {
    it?.let {
      addPlaybackFragment(it)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(mainViewModel)
    lifecycle.addObserver(searchViewModel)
    binding.mainViewModel = mainViewModel
    binding.searchViewModel = searchViewModel
    binding.setLifecycleOwner(this)
    binding.executePendingBindings()
    mainViewModel.playableContent.observe(this, contentObserver)
    val host: NavHostFragment = supportFragmentManager
        .findFragmentById(R.id.my_nav_host_fragment) as? NavHostFragment ?: return
    NavigationUI.setupWithNavController(binding.navigation, host.navController)
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(mainViewModel)
    lifecycle.removeObserver(searchViewModel)
  }

  override fun onBackPressed() {
    supportFragmentManager.findFragmentByTag(
        PlaybackFragment.TAG)?.let {
      supportFragmentManager.beginTransaction()
          .remove(it)
          .apply {
            supportFragmentManager.findFragmentByTag(DetailFragment.TAG)?.let {
              remove(it)
            }
          }
          .commit()
    } ?: run {
      super.onBackPressed()
    }
  }

  override fun remove() {
    supportFragmentManager.findFragmentByTag(
        PlaybackFragment.TAG)?.let {
      supportFragmentManager.beginTransaction()
          .remove(it)
          .apply {
            supportFragmentManager.findFragmentByTag(DetailFragment.TAG)?.let {
              remove(it)
            }
          }
          .commit()
    }
  }

  override fun clickLatestItem(video: LatestVideo) {
    mainViewModel.play(video.id)
  }

  override fun clickSearchItem(video: Content) {
    mainViewModel.play(video.id)
  }

  private fun addPlaybackFragment(id: ContentId) {
    supportFragmentManager.beginTransaction()
        .apply {
          supportFragmentManager.findFragmentByTag(
              PlaybackFragment.TAG)?.let {
            remove(it)
          }
          supportFragmentManager.findFragmentByTag(DetailFragment.TAG)?.let {
            remove(it)
          }
        }
        .add(R.id.coordinator_layout, PlaybackFragment.newInstance(id).apply {
          enterTransition = Slide().apply {
            slideEdge = BOTTOM
          }
        }, PlaybackFragment.TAG)
        .add(R.id.coordinator_layout, DetailFragment.newInstance(id).apply {
          enterTransition = Slide().apply {
            slideEdge = BOTTOM
          }
        }, DetailFragment.TAG)
        .commit()
  }
}
