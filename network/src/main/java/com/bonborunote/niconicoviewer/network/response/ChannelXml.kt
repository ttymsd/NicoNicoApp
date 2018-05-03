package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
class ChannelXml {
  @set:Element(name = "title")
  @get:Element(name = "title")
  var title: String = ""
  @set:ElementList(entry = "item", required = false, inline = true)
  @get:ElementList(entry = "item", required = false, inline = true)
  var items: ArrayList<ItemXml> = arrayListOf()
}
