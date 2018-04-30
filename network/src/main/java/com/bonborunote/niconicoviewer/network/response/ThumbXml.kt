package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "thumb", strict = false)
class ThumbXml {
  @set:Element(name = "video_id")
  @get:Element(name = "video_id")
  var videoId: String = ""
  @set:Element(name = "title")
  @get:Element(name = "title")
  var title: String = ""
  @set:Element(name = "description")
  @get:Element(name = "description")
  var description: String = ""
  @set:Element(name = "thumbnail_url")
  @get:Element(name = "thumbnail_url")
  var thumbnailUrl: String = ""
  @set:Element(name = "first_retrieve")
  @get:Element(name = "first_retrieve")
  var firstRetrieve: String = ""
  @set:Element(name = "length")
  @get:Element(name = "length")
  var length: String = ""
  @set:Element(name = "movie_type")
  @get:Element(name = "movie_type")
  var movieType: String = ""
  @set:Element(name = "size_high")
  @get:Element(name = "size_high")
  var sizeHigh: Long = 0L
  @set:Element(name = "size_low")
  @get:Element(name = "size_low")
  var sizeLow: Long = 0L
  @set:Element(name = "view_counter")
  @get:Element(name = "view_counter")
  var viewCounter: Long = 0L
  @set:Element(name = "comment_num")
  @get:Element(name = "comment_num")
  var commentNum: Long = 0L
  @set:Element(name = "mylist_counter")
  @get:Element(name = "mylist_counter")
  var myListCounter: Long = 0L
  @set:Element(name = "last_res_body")
  @get:Element(name = "last_res_body")
  var lastResBody: String = ""
  @set:Element(name = "watch_url")
  @get:Element(name = "watch_url")
  var watchUrl: String = ""
  @set:Element(name = "thumb_type")
  @get:Element(name = "thumb_type")
  var thumbType: String = ""
  @set:Element(name = "embeddable")
  @get:Element(name = "embeddable")
  var embeddable: Int = 0
  @set:Element(name = "no_live_play")
  @get:Element(name = "no_live_play")
  var noLivePlay: Int = 0
  @set:ElementList(name = "tags", required = false)
  @get:ElementList(name = "tags", required = false)
  var tags: ArrayList<Tag> = arrayListOf()
  @set:Element(name = "user_id", required = false)
  @get:Element(name = "user_id", required = false)
  var userId: String? = null
  @set:Element(name = "user_nickname", required = false)
  @get:Element(name = "user_nickname", required = false)
  var userNickname: String? = null
  @set:Element(name = "user_icon_url", required = false)
  @get:Element(name = "user_icon_url", required = false)
  var userIconUrl: String? = null
  @set:Element(name = "ch_id", required = false)
  @get:Element(name = "ch_id", required = false)
  var channelId: String? = null
  @set:Element(name = "ch_name", required = false)
  @get:Element(name = "ch_name", required = false)
  var channelName: String? = null
  @set:Element(name = "ch_icon_url", required = false)
  @get:Element(name = "ch_icon_url", required = false)
  var channelIconUrl: String? = null
}
