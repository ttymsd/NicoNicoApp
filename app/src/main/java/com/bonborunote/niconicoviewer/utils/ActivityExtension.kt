package com.bonborunote.niconicoviewer.utils

import android.app.Activity
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding

inline fun <reified T : ViewDataBinding> Activity.lazyBinding(layoutId: Int): Lazy<T> {
  return lazy { DataBindingUtil.setContentView<T>(this, layoutId) }
}

