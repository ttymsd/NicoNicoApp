package com.bonborunote.niconicoviewer.components.description

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutMetaBinding
import com.xwray.groupie.databinding.BindableItem

class MetaItem: BindableItem<LayoutMetaBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_meta
  }

  override fun bind(viewBinding: LayoutMetaBinding, position: Int) {
  }
}
