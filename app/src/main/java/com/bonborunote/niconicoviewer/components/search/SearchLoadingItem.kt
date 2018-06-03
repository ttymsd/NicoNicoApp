package com.bonborunote.niconicoviewer.components.search

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutSearchLoadingBinding
import com.xwray.groupie.databinding.BindableItem

class SearchLoadingItem : BindableItem<LayoutSearchLoadingBinding>() {

  override fun bind(viewBinding: LayoutSearchLoadingBinding, position: Int) {
    val lp = viewBinding.root.layoutParams
    if (position == 0) {
      viewBinding.root.layoutParams = lp.apply {
        width = MATCH_PARENT
        height = MATCH_PARENT
      }
    } else {
      viewBinding.root.layoutParams = lp.apply {
        width = MATCH_PARENT
        height = WRAP_CONTENT
      }
    }
  }

  override fun getLayout(): Int = R.layout.layout_search_loading
}
