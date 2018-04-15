package com.bonborunote.niconicoviewer.components.player

interface MediaUrlSource {
  fun findMediaUrl(contentId: String, callback:(mediaUrl: String) -> Unit)
}
