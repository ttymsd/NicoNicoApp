package com.bonborunote.niconicoviewer.latest.infra

import android.net.Uri
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.common.utils.convert
import com.bonborunote.niconicoviewer.latest.domain.LatestVideoRepository
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.RssApi
import com.bonborunote.niconicoviewer.network.response.ItemXml

class LatestVideoRepoitoryImpl(
  private val rssApi: RssApi
) : LatestVideoRepository {
  override fun getLatestVideos(page: Int): List<LatestVideo> {
    val response = rssApi.getLatestVideos(page).execute()
    if (300 <= response.code()) {
      throw NicoNicoException(response.code(), "detail error", response.errorBody().toString())
    }
    val xmlResponse = response?.body() ?: throw RuntimeException()
    return xmlResponse.channel?.items?.map { it.toLatestVideo() } ?: emptyList()
  }

  private fun ItemXml.toLatestVideo(): LatestVideo {
    return LatestVideo(
      id = ContentId(Uri.parse(link).lastPathSegment),
      title = title,
      thumb = ItemXml.getThumbUrl(description),
      publishDate = convert(pubDate)
    )
  }
}
