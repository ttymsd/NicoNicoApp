package com.bonborunote.niconicoviewer.components.detail

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.Identifier
import com.bonborunote.niconicoviewer.common.models.ChannelId
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.OwnerId
import com.bonborunote.niconicoviewer.common.models.RelationVideo
import com.bonborunote.niconicoviewer.common.models.Tag
import com.bonborunote.niconicoviewer.databinding.FragmentDescriptionBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class DetailFragment : Fragment(), KodeinAware {
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(DetailFragment::contentId.name, "") ?: ""
  }

  private val channelCallback: (ChannelId) -> Unit = {

  }

  private val userCallback: (OwnerId) -> Unit = {

  }

  private val relationCallback: (RelationVideo) -> Unit = {

  }

  private lateinit var binding: FragmentDescriptionBinding
  private val viewModel: DetailViewModel by instance()
  private val section = Section()
  private val relationSection = Section()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_description, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.description.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
      false)
    binding.description.adapter = GroupAdapter<ViewHolder>().apply {
      add(section)
      add(relationSection)
    }
    viewModel.detail.observe(this, Observer {
      it ?: return@Observer
      update(it)
    })
    viewModel.usersVideos.observe(this, Observer {
      it ?: return@Observer
      updateRelationItems(it)
    })
    viewModel.channelVideos.observe(this, Observer {
      it ?: return@Observer
      updateRelationItems(it)
    })
    viewModel.load(ContentId(contentId))
  }

  private fun update(detail: ContentDetail) {
    val nonNullActivity = activity ?: return
    val items = mutableListOf<Item<*>>()
    items.add(TagItem(nonNullActivity, detail.tags))
    items.add(DescriptionItem(detail.description))
    items.add(CountItem(detail.viewCount, detail.commentCount, detail.myListCount))
    detail.channel?.let {
      items.add(ChannelItem(it.id, it.name, it.thumb, channelCallback))
    }
    detail.owner?.let {
      items.add(UserItem(it.id, it.name, it.thumb, userCallback))
    }
    section.update(items)
  }

  private fun updateRelationItems(items: List<RelationVideo>) {
    relationSection.update(items.map {
      RelationVideoItem(it, relationCallback)
    })
  }

  companion object {
    const val TAG = "DetailFragment"

    fun newInstance(contentId: Identifier<String>): Fragment = DetailFragment().apply {
      arguments = Bundle().apply {
        putString(DetailFragment::contentId.name, contentId.value)
      }
    }
  }
}
