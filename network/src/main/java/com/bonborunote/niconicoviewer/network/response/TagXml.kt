package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "tag")
data class TagXml(
    @Attribute(name = "lock") val lock: String,
    @Attribute(name = "category") val category: String,
    @Element val value: String
)
