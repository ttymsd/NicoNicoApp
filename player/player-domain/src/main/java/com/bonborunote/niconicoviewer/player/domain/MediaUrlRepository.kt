package com.bonborunote.niconicoviewer.player.domain

interface MediaUrlRepository {
  fun findMediaUrl(contentId: String, callback:(mediaUrl: String) -> Unit)
}
