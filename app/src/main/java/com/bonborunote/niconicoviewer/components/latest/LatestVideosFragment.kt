package com.bonborunote.niconicoviewer.components.latest

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
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.FragmentLatestVideosBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class LatestVideosFragment : Fragment(), KodeinAware {

  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val latestViewModel: LatestViewModel by instance()
  private lateinit var binding: FragmentLatestVideosBinding
  private var listener: LatestListItemClickListener? = null
  private val latestVideosSection = PagedSection<LatestVideoItem>()
  private val section = Section().apply {
    add(latestVideosSection)
  }
  private val clickCallback: (LatestVideo) -> Unit = {
    listener?.clickLatestItem(it)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(latestViewModel)
  }

  override fun onAttach(context: Context?) {
    super.onAttach(context)
    listener = context as? LatestListItemClickListener
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater,
      R.layout.fragment_latest_videos, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.setLifecycleOwner(this)
    binding.videos.adapter = GroupAdapter<ViewHolder>().apply {
      add(section)
    }
    binding.videos.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
      false)
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
        latestVideosSection.submitList(it)
      }
    })
    latestViewModel.load(clickCallback)
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(latestViewModel)
  }

  interface LatestListItemClickListener {
    fun clickLatestItem(video: LatestVideo)
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
