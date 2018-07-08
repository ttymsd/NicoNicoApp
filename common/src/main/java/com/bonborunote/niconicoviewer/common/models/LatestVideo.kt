package com.bonborunote.niconicoviewer.common.models

import com.bonborunote.niconicoviewer.common.Entity
import com.bonborunote.niconicoviewer.common.models.ContentId
import org.threeten.bp.LocalDateTime
import java.util.Date

class LatestVideo(
  id: ContentId,
  val title: String,
  val thumb: Thumbnail,
  val publishDate: LocalDateTime
) : Entity<ContentId>(id)
