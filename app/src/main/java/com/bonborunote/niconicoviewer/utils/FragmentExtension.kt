package com.bonborunote.niconicoviewer.utils

import android.support.v4.app.Fragment

fun Fragment.showToast(resId: Int) {
  activity?.showToast(resId)
}
