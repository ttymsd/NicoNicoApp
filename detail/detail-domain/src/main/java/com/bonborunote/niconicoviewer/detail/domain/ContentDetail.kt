package com.bonborunote.niconicoviewer.detail.domain

import com.bonborunote.niconicoviewer.common.Entity

class ContentDetail(
  id: ContentId,
  val title: String,
  val description: String,
  val thumbnailUrl: String,
  val length: String,
  val viewCount: Long,
  val commentCount: Long,
  val myListCount: Long,
  val isLive: Boolean,
  val tags: List<Tag>,
  val owner: Owner? = null,
  val channel: Channel? = null
) : Entity<ContentId>(id)
