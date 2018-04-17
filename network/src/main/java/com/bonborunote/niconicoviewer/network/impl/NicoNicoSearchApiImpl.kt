package com.bonborunote.niconicoviewer.network.impl

import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.response.Content
import retrofit2.Retrofit

class NicoNicoSearchApiImpl(retrofit: Retrofit) : NicoNicoSearchApi(retrofit) {

  override fun search(keyword: String,
      targets: List<Target>,
      sort: Sort,
      fields: List<Field>,
      offset: Int,
      limit: Int,
      context: String?,
      jsonFilters: Filter?): List<Content> {
    val response = api.search(
        keyword = keyword,
        targets = targets.joinToString(",") { it.key },
        sort = sort.key,
        fields = if (fields.isEmpty()) null else fields.joinToString(",") { it.key },
        offset = offset,
        limit = limit,
        context = context,
        filters = jsonFilters?.toString()
    ).execute()

    val body = response.body() ?: throw RuntimeException()
    if (300 <= body.meta.status) {
      throw NicoNicoException(body.meta.status, body.meta.errorCode, body.meta.errorMessage)
    }
    return body.data
  }
}

