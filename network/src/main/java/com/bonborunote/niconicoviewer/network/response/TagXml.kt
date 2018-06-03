package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text

@Root(name = "tag", strict = false)
class Tag {
  @set:Attribute(name = "lock", required = false)
  @get:Attribute(name = "lock", required = false)
  var lock: String = ""
  @set:Attribute(name = "category", required = false)
  @get:Attribute(name = "category", required = false)
  var category: Int = 0
  @set:Text
  @get:Text
  var value: String = ""
}
