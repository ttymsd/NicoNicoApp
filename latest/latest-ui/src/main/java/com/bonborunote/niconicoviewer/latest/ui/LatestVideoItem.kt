package com.bonborunote.niconicoviewer.latest.ui

import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.latest.ui.databinding.LayoutLatestVideoBinding
import com.xwray.groupie.databinding.BindableItem

class LatestVideoItem(
  private val latestVideo: LatestVideo,
  private val clickCallback: (LatestVideo) -> Unit
): BindableItem<LayoutLatestVideoBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_latest_video
  }

  override fun bind(viewBinding: LayoutLatestVideoBinding, position: Int) {
    viewBinding.title = latestVideo.title
    viewBinding.thumb = latestVideo.thumb
    viewBinding.root.setOnClickListener {
      clickCallback(latestVideo)
    }
  }
}
