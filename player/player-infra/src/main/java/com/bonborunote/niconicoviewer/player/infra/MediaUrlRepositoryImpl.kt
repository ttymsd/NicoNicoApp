package com.bonborunote.niconicoviewer.player.infra

import android.content.Context
import android.view.ViewGroup
import com.bonborunote.niconicoviewer.player.domain.MediaUrlRepository

internal class MediaUrlRepositoryImpl(context: Context) : MediaUrlRepository {

  private val engine = JavaScriptEngine(context)

  override fun findMediaUrl(contentId: String, container: ViewGroup,
      callback: (mediaUrl: String) -> Unit) {
    container.addView(engine)
    engine.findMediaUrl(contentId, callback)
  }

  override fun reload(contentId: String, callback: (mediaUrl: String) -> Unit) {
    engine.findMediaUrl(contentId, callback)
  }

  override fun finalize(container: ViewGroup) {
    container.removeView(engine)
  }
}

class MediaUrlRepositoryFactory(private val context: Context) {
  fun create(): MediaUrlRepository {
    return MediaUrlRepositoryImpl(context)
  }
}
