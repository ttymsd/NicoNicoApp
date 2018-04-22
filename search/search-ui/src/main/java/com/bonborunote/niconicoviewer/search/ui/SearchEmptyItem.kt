package com.bonborunote.niconicoviewer.search.ui

import com.bonborunote.niconicoviewer.search.R
import com.bonborunote.niconicoviewer.search.databinding.LayoutSearchEmptyBinding
import com.xwray.groupie.databinding.BindableItem

class SearchEmptyItem : BindableItem<LayoutSearchEmptyBinding>() {

  override fun bind(viewBinding: LayoutSearchEmptyBinding, position: Int) {
  }

  override fun getLayout(): Int = R.layout.layout_search_empty
}
