package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "thumb")
data class ThumbXml(
    @Element(name = "video_id") val videoId: String,
    @Element(name = "title") val title: String,
    @Element(name = "description") val description: String
)
