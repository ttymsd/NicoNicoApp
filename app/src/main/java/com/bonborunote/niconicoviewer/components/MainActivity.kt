package com.bonborunote.niconicoviewer.components

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bonborunote.niconicoviewer.R
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

class MainActivity : AppCompatActivity(), KodeinAware {

  private val _parentKodein by closestKodein()
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by retainedKodein {
    extend(_parentKodein, copy = All)
  }

  private val binding by lazyBinding<ActivityMainBinding>(R.layout.activity_main)
  private val mainViewModel: MainViewModel by instance()
  private val searchViewModel: SearchViewModel by instance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(mainViewModel)
    binding.mainViewModel = mainViewModel
    binding.searchViewModel = searchViewModel
    binding.setLifecycleOwner(this)
    binding.executePendingBindings()

    if (supportFragmentManager.findFragmentById(R.id.container) == null) {
      supportFragmentManager.beginTransaction()
          .add(R.id.container, SearchContainer.newInstance())
          .commit()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(mainViewModel)
  }
}
