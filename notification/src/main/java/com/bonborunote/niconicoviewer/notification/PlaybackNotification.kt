package com.bonborunote.niconicoviewer.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.bonborunote.niconicoviewer.notification.R.string

const val PLAYBACK_NOTIFICATION_CHANNEL_ID = "playback"

fun Context.createPlaybackChannel() {
  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
  val channel = NotificationChannel(PLAYBACK_NOTIFICATION_CHANNEL_ID,
      getString(string.background_playback_channel_name),
      NotificationManager.IMPORTANCE_DEFAULT)
  getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
}

fun Context.createPlaybackNotification(): Notification {
  return NotificationCompat.Builder(this, PLAYBACK_NOTIFICATION_CHANNEL_ID)
      .setSmallIcon(R.drawable.notification_icon_background)
      .setContentTitle("バックグラウンド再生")
      .setContentText("hello")
      .build()
}
