package com.bonborunote.niconicoviewer.common.models

import com.bonborunote.niconicoviewer.common.Entity
import com.bonborunote.niconicoviewer.common.models.ContentId
import java.util.Date

class LatestVideo(
  id: ContentId,
  val title: String,
  val thumb: String,
  val publishDate: Date
) : Entity<ContentId>(id)
