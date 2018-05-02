package com.bonborunote.niconicoviewer.search.ui

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bonborunote.niconicoviewer.search.domain.ContentId
import com.bumptech.glide.Glide

@BindingAdapter("load_image_from_id")
fun ImageView.load(contentId: ContentId) {
  val base = "http://tn.smilevideo.jp/smile?i="
  Glide.with(context)
      .load("$base${contentId.value.substring(2, contentId.value.length)}")
      .into(this)
}


