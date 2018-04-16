package com.bonborunote.niconicoviewer.components.description

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.FragmentDescriptionBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.kcontext

class DescriptionFragment : Fragment(), KodeinAware {
  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val contentId: String by lazy {
    arguments?.getString(DescriptionFragment::contentId.name, "") ?: ""
  }

  private lateinit var binding: FragmentDescriptionBinding
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
  }

  companion object {
    const val TAG = "DescriptionFragment"

    fun newInstance(contentId: String): Fragment = DescriptionFragment().apply {
      arguments = Bundle().apply {
        putString(DescriptionFragment::contentId.name, contentId)
      }
    }
  }
}
