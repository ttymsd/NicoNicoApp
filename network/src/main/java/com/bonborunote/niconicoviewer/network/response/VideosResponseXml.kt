package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class VideosResponseXml {
  @set:Element(name = "channel", required = false)
  @get:Element(name = "channel", required = false)
  var channel: ChannelXml? = null
}
