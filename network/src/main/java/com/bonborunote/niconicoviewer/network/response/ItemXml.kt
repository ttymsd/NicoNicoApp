package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "channel", strict = false)
class ItemXml {
  @set:Element(name = "title")
  @get:Element(name = "title")
  var title: String = ""
  @set:Element(name = "link")
  @get:Element(name = "link")
  var link: String = ""
  @set:Element(name = "description")
  @get:Element(name = "description")
  var description: String = ""
  @set:Element(name = "pubDate")
  @get:Element(name = "pubDate")
  var pubDate: String = ""

  companion object {
    private val THUMB_URL_REGEX = Regex("https?://tn\\.smilevideo\\.jp/smile\\?i=[0-9]+")

    fun getThumbUrl(description: String): String {
      return THUMB_URL_REGEX.findAll(description).firstOrNull()?.value ?: ""
    }
  }
}
