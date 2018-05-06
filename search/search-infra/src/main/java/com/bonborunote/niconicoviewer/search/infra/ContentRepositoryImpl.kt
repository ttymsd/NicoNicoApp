package com.bonborunote.niconicoviewer.search.infra

import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.common.models.ContentId
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Target
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.CONTENT_ID
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.LENGTH_SECONDS
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.TITLE
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Target.DESCRIPTION
import com.bonborunote.niconicoviewer.search.domain.ContentRepository
import com.bonborunote.niconicoviewer.search.domain.ContentRepository.Filter
import com.bonborunote.niconicoviewer.search.domain.Sort
import com.bonborunote.niconicoviewer.search.domain.Sort.*

class ContentRepositoryImpl(private val api: NicoNicoSearchApi) : ContentRepository {

  override fun search(keyword: String,
      sort: Sort,
      offset: Int,
      limit: Int,
      context: String?,
      jsonFilters: Filter?): List<Content> {
    val response = api.searchVideo(
        keyword = keyword,
        targets = listOf(Target.TITLE, DESCRIPTION, Target.TAGS).joinToString(",") { it.key },
        sort = sort.convertToNetwork().key,
        fields = listOf(CONTENT_ID, TITLE, LENGTH_SECONDS).joinToString(",") { it.key },
        offset = offset,
        limit = limit,
        context = context,
        filters = jsonFilters?.toString()
    ).execute()

    val body = response.body() ?: throw RuntimeException()
    if (300 <= body.metaJson.status) {
      throw NicoNicoException(body.metaJson.status, body.metaJson.errorCode,
          body.metaJson.errorMessage)
    }
    return body.data.map {
      Content(
          id = ContentId(it.contentId),
          title = it.title,
          lengthSeconds = it.lengthSeconds
      )
    }
  }
}

private fun Sort.convertToNetwork(): NicoNicoSearchApi.Sort {
  return when (this) {
    VIEW_COUNT_ASC -> NicoNicoSearchApi.Sort.VIEW_COUNT_ASC
    VIEW_COUNT_DESC -> NicoNicoSearchApi.Sort.VIEW_COUNT_DESC
    MYLIST_COUNT_ASC -> NicoNicoSearchApi.Sort.MYLIST_COUNT_ASC
    MYLIST_COUNT_DESC -> NicoNicoSearchApi.Sort.MYLIST_COUNT_DESC
    COMMENT_COUNT_ASC -> NicoNicoSearchApi.Sort.COMMENT_COUNT_ASC
    COMMENT_COUNT_DESC -> NicoNicoSearchApi.Sort.COMMENT_COUNT_DESC
    START_TIME_COUNT_ASC -> NicoNicoSearchApi.Sort.START_TIME_COUNT_ASC
    START_TIME_COUNT_DESC -> NicoNicoSearchApi.Sort.START_TIME_COUNT_DESC
    LAST_COMMENT_TIME_COUNT_ASC -> NicoNicoSearchApi.Sort.LAST_COMMENT_TIME_COUNT_ASC
    LAST_COMMENT_TIME_COUNT_DESC -> NicoNicoSearchApi.Sort.LAST_COMMENT_TIME_COUNT_DESC
    LENGTH_SECONDS_COUNT_ASC -> NicoNicoSearchApi.Sort.LENGTH_SECONDS_COUNT_ASC
    LENGTH_SECONDS_COUNT_DESC -> NicoNicoSearchApi.Sort.LENGTH_SECONDS_COUNT_DESC
  }
}
