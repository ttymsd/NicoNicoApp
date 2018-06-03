package com.bonborunote.niconicoviewer.bindings

import android.databinding.BindingAdapter
import android.support.v7.widget.SearchView
import android.support.v7.widget.SearchView.OnQueryTextListener

@BindingAdapter("query_text_listener")
fun SearchView.addListener(listener: OnQueryTextListener) {
  this.setOnQueryTextListener(listener)
}
