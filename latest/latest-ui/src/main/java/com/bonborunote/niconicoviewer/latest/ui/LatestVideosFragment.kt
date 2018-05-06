package com.bonborunote.niconicoviewer.latest.ui

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.latest.ui.databinding.FragmentLatestVideosBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class LatestVideosFragment: Fragment(), KodeinAware {

  // instance作った時点ではactivity = null なので getterをoverrideする
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val latestViewModel: LatestViewModel by instance()
  private lateinit var binding: FragmentLatestVideosBinding
  private val section = Section()
  private val clickCallback: (LatestVideo) -> Unit = {

  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(latestViewModel)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_latest_videos, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.setLifecycleOwner(this)
    binding.videos.adapter = GroupAdapter<ViewHolder>().apply {
      add(section)
    }
    binding.videos.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    binding.executePendingBindings()
    latestViewModel.loading.observe(this, Observer { loading ->
      if (loading == true) {
//        section.setFooter(SearchLoadingItem())
      } else {
//        section.removeFooter()
      }
    })
    latestViewModel.videos.observe(this, Observer {
      it?.let {
        section.update(it.map { LatestVideoItem(it, clickCallback) })
      }
    })
    latestViewModel.load()
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(latestViewModel)
  }

  companion object {
    const val TAG = "LatestVideoFragment"

    fun newInstance(): LatestVideosFragment {
      return LatestVideosFragment().apply {
        arguments = Bundle().apply {
        }
      }
    }
  }
}
