package com.bonborunote.niconicoviewer.components.detail

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutDescriptionBinding
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
