package com.bonborunote.niconicoviewer.utils

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.widget.Toast

inline fun <reified T : ViewDataBinding> Activity.lazyBinding(layoutId: Int): Lazy<T> {
  return lazy { DataBindingUtil.setContentView<T>(this, layoutId) }
}
