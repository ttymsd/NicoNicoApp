package com.bonborunote.niconicoviewer.components.detail

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutTitleBinding
import com.xwray.groupie.databinding.BindableItem

class TitleItem(
    private val title: String
) : BindableItem<LayoutTitleBinding>() {

  override fun getLayout(): Int {
    return R.layout.layout_title
  }

  override fun bind(viewBinding: LayoutTitleBinding, position: Int) {
    viewBinding.title = title
  }
}
