package com.bonborunote.niconicoviewer.detail.ui

import com.bonborunote.niconicoviewer.detail.ui.databinding.LayoutDescriptionBinding
import com.xwray.groupie.databinding.BindableItem

class DescriptionItem(
  private val description: String
) : BindableItem<LayoutDescriptionBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_description
  }

  override fun bind(viewBinding: LayoutDescriptionBinding, position: Int) {
    viewBinding.description = description
  }
}
