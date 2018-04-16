package com.bonborunote.niconicoviewer.bindings

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

@BindingAdapter("load_image")
fun ImageView.load(contentId: String) {
  val base = "http://tn.smilevideo.jp/smile?i="
  Glide.with(context)
      .load("$base${contentId.substring(2, contentId.length)}")
      .into(this)
}


