package com.bonborunote.niconicoviewer.components.detail

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.models.OwnerId
import com.bonborunote.niconicoviewer.databinding.LayoutOwnerBinding
import com.xwray.groupie.databinding.BindableItem

class UserItem(
  private val userId: OwnerId,
  private val name: String,
  private val thumb: String,
  private val callback: (OwnerId) -> Unit
) : BindableItem<LayoutOwnerBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_owner
  }

  override fun bind(viewBinding: LayoutOwnerBinding, position: Int) {
    viewBinding.name = name
    viewBinding.thumb = thumb
    viewBinding.root.setOnClickListener {
      callback(userId)
    }
  }
}
