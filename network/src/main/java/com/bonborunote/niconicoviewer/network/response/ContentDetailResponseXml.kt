package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "nicovideo_thumb_response")
data class ContentDetailResponseXml(
    @Attribute val status: String,
    @Path("thumb") val thumbXml: ThumbXml
)
