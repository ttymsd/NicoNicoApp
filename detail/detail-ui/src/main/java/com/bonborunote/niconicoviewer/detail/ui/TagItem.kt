package com.bonborunote.niconicoviewer.detail.ui

import com.bonborunote.niconicoviewer.detail.ui.databinding.LayoutTagBinding
import com.xwray.groupie.databinding.BindableItem

class TagItem: BindableItem<LayoutTagBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_tag
  }

  override fun bind(viewBinding: LayoutTagBinding, position: Int) {
  }
}
