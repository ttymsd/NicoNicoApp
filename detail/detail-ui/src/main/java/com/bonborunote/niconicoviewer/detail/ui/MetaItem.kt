package com.bonborunote.niconicoviewer.detail.ui

import com.bonborunote.niconicoviewer.detail.ui.databinding.LayoutMetaBinding
import com.xwray.groupie.databinding.BindableItem

class MetaItem: BindableItem<LayoutMetaBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_meta
  }

  override fun bind(viewBinding: LayoutMetaBinding, position: Int) {
  }
}
