package com.bonborunote.niconicoviewer.utils

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.support.annotation.StringRes
import android.widget.Toast

fun Context.showToast(resId: Int) {
  Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun Context.checkOverlayPermission(): Boolean {
  if (Build.VERSION.SDK_INT < 23) {
    return true
  }
  return Settings.canDrawOverlays(this)
}
