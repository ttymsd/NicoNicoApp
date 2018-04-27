package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "nicovideo_thumb_response", strict = false)
data class ContentDetailResponseXml(
    @get:Attribute(name = "status")
    @set:Attribute(name = "status")
    var status: String,

    @set:Element(name = "thumb")
    @get:Element(name = "thumb")
    var thumbXml: ThumbXml
)
