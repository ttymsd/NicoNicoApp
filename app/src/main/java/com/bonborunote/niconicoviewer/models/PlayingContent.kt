package com.bonborunote.niconicoviewer.models

import com.bonborunote.niconicoviewer.common.models.ContentId

data class PlayingContent(
    val contentId: ContentId,
    val title: String,
    val thumbnail: String
)
