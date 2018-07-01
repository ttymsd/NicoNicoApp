package com.bonborunote.niconicoviewer.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.bonborunote.niconicoviewer.notification.R.string

const val PLAYBACK_NOTIFICATION_CHANNEL_ID = "playback"

fun Context.createPlaybackChannel() {
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
  val channel = NotificationChannel(PLAYBACK_NOTIFICATION_CHANNEL_ID,
      getString(string.background_playback_channel_name),
      NotificationManager.IMPORTANCE_DEFAULT)
  getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
}
