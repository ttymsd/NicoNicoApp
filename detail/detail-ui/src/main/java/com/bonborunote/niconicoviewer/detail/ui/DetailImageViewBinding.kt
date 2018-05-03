package com.bonborunote.niconicoviewer.detail.ui

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bonborunote.niconicoviewer.detail.domain.ContentId
import com.bumptech.glide.Glide

@BindingAdapter("load_image_relation_id")
fun ImageView.load(id: ContentId) {
  val base = "http://tn.smilevideo.jp/smile?i="
  Glide.with(context)
    .load("$base${id.value.substring(2, id.value.length)}")
    .into(this)
}
