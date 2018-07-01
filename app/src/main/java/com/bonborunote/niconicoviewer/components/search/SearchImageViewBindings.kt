package com.bonborunote.niconicoviewer.components.search

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bumptech.glide.Glide

@BindingAdapter("load_image_from_id")
fun ImageView.load(contentId: ContentId) {
  Glide.with(context)
      .load(contentId.getThumbnail())
      .into(this)
}

fun ContentId.getThumbnail(): String {
  val base = "http://tn.smilevideo.jp/smile?i="
  return "$base${value.substring(2, value.length)}"
}


