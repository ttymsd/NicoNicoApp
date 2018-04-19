package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.groupie.aac.plugin.PagedSection
import com.bonborunote.groupie.aac.plugin.PagedSection2
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.FragmentSearchBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SearchContainer : Fragment(), KodeinAware {

  // instance作った時点ではactivity = null なので getterをoverrideする
  override val kodeinContext: KodeinContext<*>
    get() = kcontext(activity)
  override val kodein: Kodein by closestKodein()

  private lateinit var binding: FragmentSearchBinding

  private val searchViewModel: SearchViewModel by instance()
  private val section = PagedSection2<SearchContentItem>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(searchViewModel)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.setLifecycleOwner(this)
    binding.list.adapter = GroupAdapter<ViewHolder>().apply {
      add(section)
    }
    binding.list.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    binding.executePendingBindings()
    searchViewModel.loading.observe(this, Observer { loading ->
//      if (loading == true) {
//        section.setFooter(SearchLoadingItem())
//      } else {
//        section.removeFooter()
//      }
    })
    searchViewModel.contents.observe(this, Observer {
      it?.let {
        Log.d("OkHttp", "submitList")
        section.submitList(it)
      }
    })
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(searchViewModel)
  }

  companion object {
    const val TAG = "SearchContainer"

    fun newInstance(): SearchContainer = SearchContainer().apply {
      arguments = Bundle()
    }
  }
}
