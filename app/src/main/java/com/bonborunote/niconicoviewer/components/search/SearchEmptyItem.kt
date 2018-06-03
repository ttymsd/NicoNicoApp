package com.bonborunote.niconicoviewer.components.search

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutSearchEmptyBinding
import com.xwray.groupie.databinding.BindableItem

class SearchEmptyItem : BindableItem<LayoutSearchEmptyBinding>() {

  override fun bind(viewBinding: LayoutSearchEmptyBinding, position: Int) {
  }

  override fun getLayout(): Int = R.layout.layout_search_empty
}
