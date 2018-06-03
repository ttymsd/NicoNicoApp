package com.bonborunote.niconicoviewer.components.detail

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bumptech.glide.Glide

@BindingAdapter("load_image_relation_id")
fun ImageView.load(id: ContentId) {
  val base = "http://tn.smilevideo.jp/smile?i="
  Glide.with(context)
    .load("$base${id.value.substring(2, id.value.length)}")
    .into(this)
}
