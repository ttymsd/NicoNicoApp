package com.bonborunote.niconicoviewer.components.detail

import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.common.models.RelationVideo
import com.bonborunote.niconicoviewer.databinding.LayoutRelationVideoBinding
import com.xwray.groupie.databinding.BindableItem

class RelationVideoItem(
  private val relationVideo: RelationVideo,
  private val callback: (RelationVideo) -> Unit
) : BindableItem<LayoutRelationVideoBinding>() {
  override fun getLayout(): Int {
    return R.layout.layout_relation_video
  }

  override fun bind(viewBinding: LayoutRelationVideoBinding, position: Int) {
    viewBinding.thumb = relationVideo.thumb.smallThumbnail
    viewBinding.title = relationVideo.title
    viewBinding.root.setOnClickListener {
      callback(relationVideo)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (other !is RelationVideoItem) return false
    return other.relationVideo.id == relationVideo.id
  }

  override fun hashCode(): Int {
    return relationVideo.id.hashCode()
  }
}
