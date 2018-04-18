package com.bonborunote.niconicoviewer.repositories

import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.CONTENT_ID
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.LENGTH_SECONDS
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Field.TAG
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Sort.VIEW_COUNT_DESC
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Target.TITLE
import com.bonborunote.niconicoviewer.network.response.Content
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class SearchResultRepository(
    val nicoNicoSearchApi: NicoNicoSearchApi
) {
  val loading = BehaviorSubject.createDefault(false)
  val error = BehaviorSubject.create<NicoNicoException>()
  val results = BehaviorSubject.create<List<Content>>()

  fun search(keyword: String, offset: Int, pageSize: Int = DEFAULT_PAGE_SIZE) {
    async(CommonPool) {
      try {
        loading.onNext(true)
        val result = nicoNicoSearchApi.search(
            keyword = keyword,
            targets = listOf(TITLE),
            sort = VIEW_COUNT_DESC,
            fields = listOf(CONTENT_ID, Field.TITLE, TAG, LENGTH_SECONDS),
            offset = offset,
            limit = pageSize)
        loading.onNext(false)
        results.onNext(result)
      } catch (e: NicoNicoException) {
        error.onNext(e)
        loading.onNext(false)
      } catch (e: Exception) {
        loading.onNext(false)
      }
    }
  }

  companion object {
    private val DEFAULT_PAGE_SIZE = 20
  }
}
