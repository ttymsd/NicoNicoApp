package com.bonborunote.niconicoviewer.player.infra

import android.view.ViewGroup
import com.bonborunote.niconicoviewer.player.domain.MediaUrlRepository

internal class MediaUrlRepositoryImpl : MediaUrlRepository {
  override fun findMediaUrl(contentId: String, container: ViewGroup,
      callback: (mediaUrl: String) -> Unit) {
    val engine = JavaScriptEngine(container.context)
    container.addView(engine)
    engine.findMediaUrl(contentId, callback)
  }
}

class MediaUrlRepositoryFactory {
  fun create(): MediaUrlRepository {
    return MediaUrlRepositoryImpl()
  }
}
