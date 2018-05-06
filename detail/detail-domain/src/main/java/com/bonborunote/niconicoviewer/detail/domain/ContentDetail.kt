package com.bonborunote.niconicoviewer.detail.domain

import com.bonborunote.niconicoviewer.common.Entity
import com.bonborunote.niconicoviewer.common.models.Channel
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.Owner
import com.bonborunote.niconicoviewer.common.models.Tag

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
