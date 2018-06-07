package com.bonborunote.niconicoviewer.components.playback

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.session.MediaSessionCompat
import com.bonborunote.niconicoviewer.BuildConfig
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.KodeinContext
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.kcontext

class MediaSessionService: MediaBrowserServiceCompat(), KodeinAware {

  override val kodeinContext: KodeinContext<*> = kcontext(this)
  override val kodein: Kodein by closestKodein()

  private val mediaSessionCompat = MediaSessionCompat(this, MEDIA_SESSION_TAG)

  override fun onCreate() {
    super.onCreate()
    sessionToken = mediaSessionCompat.sessionToken
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  override fun onGetRoot(
    clientPackageName: String,
    clientUid: Int,
    rootHints: Bundle?): BrowserRoot? {
    return if (BuildConfig.APPLICATION_ID == clientPackageName) {
      BrowserRoot(ROOT_MEDIA_ID, null)
    } else {
      null
    }
  }

  override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
    TODO(
      "not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  companion object {
    private const val ROOT_MEDIA_ID = "hoge"
    private const val MEDIA_SESSION_TAG = "media"
  }
}
