package com.bonborunote.niconicoviewer.detail.ui

import com.bonborunote.niconicoviewer.detail.ui.databinding.LayoutCountBinding
import com.xwray.groupie.databinding.BindableItem

class CountItem(
  private val viewCount: Long,
  private val commentCount: Long,
  private val myListCount: Long
) : BindableItem<LayoutCountBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_count
  }

  override fun bind(viewBinding: LayoutCountBinding, position: Int) {
    viewBinding.viewCount = viewCount
    viewBinding.commentCount = commentCount
    viewBinding.myListCount = myListCount
  }
}
