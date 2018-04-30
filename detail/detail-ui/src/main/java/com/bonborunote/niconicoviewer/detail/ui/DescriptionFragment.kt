package com.bonborunote.niconicoviewer.detail.ui

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.common.Identifier
import com.bonborunote.niconicoviewer.detail.domain.ContentDetail
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bonborunote.niconicoviewer.detail.ui.databinding.FragmentDescriptionBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class DescriptionFragment : Fragment(), KodeinAware {
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(DescriptionFragment::contentId.name, "") ?: ""
  }

  private lateinit var binding: FragmentDescriptionBinding
  private val viewModel:DetailViewModel by instance()
  private val section = Section()

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
    }
    viewModel.detail.observe(this, Observer {
      it ?: return@Observer
      update(it)
    })
    viewModel.load(ContentId(contentId))
  }

  private fun update(detail : ContentDetail) {
  }

  companion object {
    const val TAG = "DescriptionFragment"

    fun newInstance(contentId: Identifier<String>): Fragment = DescriptionFragment().apply {
      arguments = Bundle().apply {
        putString(DescriptionFragment::contentId.name, contentId.value)
      }
    }
  }
}
