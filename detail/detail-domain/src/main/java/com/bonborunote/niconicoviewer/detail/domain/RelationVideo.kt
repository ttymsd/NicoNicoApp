package com.bonborunote.niconicoviewer.detail.domain

import com.bonborunote.niconicoviewer.common.Entity

class RelationVideo(
  id: ContentId,
  val title: String,
  val thumb: String
) : Entity<ContentId>(id)
