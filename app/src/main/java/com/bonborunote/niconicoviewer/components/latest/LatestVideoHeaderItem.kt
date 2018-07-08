package com.bonborunote.niconicoviewer.components.latest

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutLatestVideoHeaderBinding
import com.xwray.groupie.databinding.BindableItem

class LatestVideoHeaderItem : BindableItem<LayoutLatestVideoHeaderBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_latest_video_header
  }

  override fun bind(viewBinding: LayoutLatestVideoHeaderBinding, position: Int) {
  }
}

