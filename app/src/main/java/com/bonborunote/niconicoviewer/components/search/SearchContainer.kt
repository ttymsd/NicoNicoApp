package com.bonborunote.niconicoviewer.components.search

import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.groupie.aac.plugin.PagedSection
import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.FragmentSearchBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
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
  private var listener: SearchListItemClickListener? = null

  private val searchViewModel: SearchViewModel by instance()
  private val searchContentSection = PagedSection<SearchContentItem>()
  private val section = Section().apply {
    add(searchContentSection)
  }

  private val keyword by lazy {
    SearchContainerArgs.fromBundle(arguments).keyword
  }
  private val tagWord by lazy {
    SearchContainerArgs.fromBundle(arguments).tag
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(searchViewModel)
    when {
      keyword.isNotBlank() -> searchViewModel.search(keyword)
      tagWord.isNotBlank() -> searchViewModel.searchFromTag(tagWord)
      else -> Unit
    }
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    listener = context as? SearchListItemClickListener
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
      if (loading == true) {
        section.setFooter(SearchLoadingItem())
      } else {
        section.removeFooter()
      }
    })
    searchViewModel.searchContents.observe(this, Observer {
      it?.let {
        searchContentSection.submitList(it)
      }
    })
    searchViewModel.tagSearchContents.observe(this, Observer {
      it?.let { searchContentSection.submitList(it)}
    })
    searchViewModel.playableContent.observe(this, Observer {
      it?.let {
        listener?.clickSearchItem(it)
      }
    })
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(searchViewModel)
  }

  interface SearchListItemClickListener {
    fun clickSearchItem(video: Content)
  }
}
