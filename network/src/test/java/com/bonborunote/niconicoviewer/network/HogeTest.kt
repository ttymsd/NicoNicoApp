package com.bonborunote.niconicoviewer.network

import com.bonborunote.niconicoviewer.network.response.TagXml
import com.bonborunote.niconicoviewer.network.response.ThumbXml
import org.junit.Test
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class HogeTest {
  @Test fun hoge() {
    val tags = listOf<TagXml>()
    val xml = ThumbXml(
      "videoId", "title", "description", "thumbnailUrl", "firstRetrive", "length",
      "movieType", 1000, 0, 0, 1, 2, "lastRes", "watch", "thumbType",
      "1", "2", tags, "userId", "nickname", "iconUrl", "channelId",
      "name", "iconUrl"
    )
  }
}
