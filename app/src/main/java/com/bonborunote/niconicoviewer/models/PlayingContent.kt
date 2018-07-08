package com.bonborunote.niconicoviewer.models

import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.Thumbnail

data class PlayingContent(
    val contentId: ContentId,
    val title: String,
    val thumbnail: Thumbnail
)
