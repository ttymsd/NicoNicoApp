package com.bonborunote.niconicoviewer.common

import android.os.Build

fun higherMashmallow(): Boolean {
  return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}
