package com.bonborunote.niconicoviewer

import android.content.Context

class Preference(
    val context: Context
) {

  private val preference = context.getSharedPreferences("nico", Context.MODE_PRIVATE)

  fun backgroundPlaybackEnable(): Boolean {
    return preference.getBoolean("background_playback_enable", false)
  }

  fun updateBackgroundPlaybackEnable(enable: Boolean) {
    preference.edit().putBoolean("background_playback_enable", enable).apply()
  }

  fun pictureInPictureEnable(): Boolean {
    return preference.getBoolean("picture_in_picture_enable", false)
  }

  fun updatePictureInPictureEnable(enable: Boolean) {
    preference.edit().putBoolean("picture_in_picture_enable", enable).apply()
  }
}
