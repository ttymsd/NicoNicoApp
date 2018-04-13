package com.bonborunote.niconicoviewer.components.search

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutSearchBinding
import com.bonborunote.niconicoviewer.network.response.Content
import com.xwray.groupie.databinding.BindableItem

data class SearchContentItem(
    private val content: Content
) : BindableItem<LayoutSearchBinding>() {
  override fun bind(viewBinding: LayoutSearchBinding, position: Int) {
    viewBinding.content = content
    viewBinding.executePendingBindings()
  }

  override fun getLayout(): Int = R.layout.layout_search
}
