package com.bonborunote.niconicoviewer.player.domain

import android.support.annotation.MainThread
import android.view.ViewGroup

interface MediaUrlRepository {
  @MainThread
  fun findMediaUrl(contentId: String, container: ViewGroup, callback: (mediaUrl: String) -> Unit)
}
