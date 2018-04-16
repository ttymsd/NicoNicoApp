package com.bonborunote.niconicoviewer.bindings

import android.databinding.BindingAdapter
import android.widget.TextView
import org.threeten.bp.Duration

@BindingAdapter("text_seconds")
fun TextView.addListener(seconds: Long) {
  val duration = Duration.ofSeconds(seconds)
  val hours = duration.toHours()
  val minutes = duration.minusHours(hours).toMinutes()
  text = String.format("%02d:%02d", hours, minutes)
}
