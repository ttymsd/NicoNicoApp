package com.bonborunote.niconicoviewer.common.models

import com.bonborunote.niconicoviewer.common.Entity

class Content(
    id: ContentId,
    val title: String = "",
    val lengthSeconds: Long = 1200L
) : Entity<ContentId>(id)