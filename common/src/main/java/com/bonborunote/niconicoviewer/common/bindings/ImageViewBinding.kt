package com.bonborunote.niconicoviewer.common.bindings

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("circle_image_uri")
fun ImageView.loadImage(uri: String) {
  Glide.with(this)
    .load(uri)
    .apply(RequestOptions().circleCrop())
    .into(this)
}

@BindingAdapter("image_uri")
fun ImageView.load(uri: String) {
  Glide.with(context)
    .load(uri)
    .into(this)
}
