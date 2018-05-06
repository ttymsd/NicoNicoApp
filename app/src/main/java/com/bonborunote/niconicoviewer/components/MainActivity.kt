package com.bonborunote.niconicoviewer.components

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.databinding.ActivityMainBinding
import com.bonborunote.niconicoviewer.detail.ui.DetailFragment
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.latest.ui.LatestVideosFragment
import com.bonborunote.niconicoviewer.player.ui.PlaybackFragment
import com.bonborunote.niconicoviewer.player.ui.PlaybackFragment.OnPlayerStateChangedListener
import com.bonborunote.niconicoviewer.search.ui.SearchViewModel
import com.bonborunote.niconicoviewer.utils.lazyBinding
import org.kodein.di.Copy.All
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class MainActivity : AppCompatActivity(), KodeinAware, OnPlayerStateChangedListener {
  private val _parentKodein by closestKodein()
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by retainedKodein {
    extend(_parentKodein, copy = All)
  }

  private val binding by lazyBinding<ActivityMainBinding>(R.layout.activity_main)
  private val mainViewModel: MainViewModel by instance()
  private val searchViewModel: SearchViewModel by instance()
  private val contentObserver = Observer<Content> {
    it?.let { content ->
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
          .add(R.id.coordinator_layout, PlaybackFragment.newInstance(
              ContentId(content.id.value)),
              PlaybackFragment.TAG)
          .add(R.id.coordinator_layout, DetailFragment.newInstance(content.id),
              DetailFragment.TAG)
          .commit()
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
    searchViewModel.playableContent.observe(this, contentObserver)

    if (supportFragmentManager.findFragmentById(R.id.coordinator_layout) == null) {
      supportFragmentManager.beginTransaction()
          .add(R.id.coordinator_layout, LatestVideosFragment.newInstance(), LatestVideosFragment.TAG)
          .commit()
    }
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
}
