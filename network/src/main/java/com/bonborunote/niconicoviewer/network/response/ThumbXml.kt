package com.bonborunote.niconicoviewer.network.response

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "thumb", strict = false)
data class ThumbXml(
    @Element(name = "video_id") val videoId: String,
    @Element(name = "title") val title: String,
    @Element(name = "description") val description: String,
    @Element(name = "thumbnail_url") val thumbnailUrl: String,
    @Element(name = "first_retrieve") val firstRetrieve: String,
    @Element(name = "length") val length: String,
    @Element(name = "movie_type") val movieType: String,
    @Element(name = "size_high") val sizeHigh: Long,
    @Element(name = "size_low") val sizeLow: Long,
    @Element(name = "view_counter") val viewCounter: Long,
    @Element(name = "comment_num") val commentNum: Long,
    @Element(name = "mylist_counter") val myListCounter: Long,
    @Element(name = "watch_url") val watchUrl: String,
    @Element(name = "thumb_type") val thumbType: String,
    @Element(name = "embeddable") val embeddable: String,
    @Element(name = "no_live_play") val noLivePlay: String,
    @ElementList(name = "tags", inline = true) val tags: List<TagXml>,
    @Element(name = "ch_id") val channelId: String,
    @Element(name = "ch_name") val channelName: String,
    @Element(name = "ch_icon_url") val channelIconUrl: String
)
