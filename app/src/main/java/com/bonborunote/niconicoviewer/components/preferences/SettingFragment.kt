package com.bonborunote.niconicoviewer.components.preferences

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.higherOreo
import com.bonborunote.niconicoviewer.databinding.FragmentSettingBinding
import com.bonborunote.niconicoviewer.utils.showToast
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext

class SettingFragment : Fragment(), KodeinAware {

  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()
  private lateinit var binding: FragmentSettingBinding
  private val adapter = GroupAdapter<ViewHolder>()
  private val settingSection = Section()

  private val preferenceViewModel: PreferenceViewModel by instance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycle.addObserver(preferenceViewModel)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    binding.setLifecycleOwner(this)
    binding.settingList.itemAnimator = null
    binding.settingList.adapter = adapter
    adapter.add(settingSection)
    binding.settingList.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
        false)
    preferenceViewModel.observeBackgroundPlaybackEnable(this, Observer {
      it ?: return@Observer
      reload()
      showToast(if (it) {
        R.string.background_playback_enable
      } else {
        R.string.background_playback_disable
      })
    })
    preferenceViewModel.observePictureInPictureEnable(this, Observer {
      it ?: return@Observer
      reload()
      showToast(if (it) {
        R.string.pip_enable
      } else {
        R.string.pip_disable
      })
    })
    reload()
  }

  override fun onDestroy() {
    super.onDestroy()
    lifecycle.removeObserver(preferenceViewModel)
  }

  private fun reload() {
    val groups = arrayListOf<Group>()
    groups.add(
        BackgroundPlaybackSetting(preferenceViewModel.backgroundPlaybackEnable()) {
          preferenceViewModel.updateBackgroundPlaybackEnable(it)
        })

    if (higherOreo()) {
      groups.add(
          PictureInPictureSetting(preferenceViewModel.pictureInPictureEnable()) {
            preferenceViewModel.updatePictureInPictureEnable(it)
          }
      )
    }

    settingSection.update(groups)
  }
}
