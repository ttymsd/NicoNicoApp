package com.bonborunote.niconicoviewer.components.detail

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.models.ChannelId
import com.bonborunote.niconicoviewer.databinding.LayoutOwnerBinding
import com.xwray.groupie.databinding.BindableItem

class ChannelItem(
  private val channelId: ChannelId,
  private val name: String,
  private val thumb: String,
  private val callback: (ChannelId) -> Unit
) : BindableItem<LayoutOwnerBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_owner
  }

  override fun bind(viewBinding: LayoutOwnerBinding, position: Int) {
    viewBinding.name = name
    viewBinding.thumb = thumb
    viewBinding.root.setOnClickListener {
      callback(channelId)
    }
  }
}
