package com.bonborunote.niconicoviewer.repositories

import com.bonborunote.niconicoviewer.network.NicoNicoApi
import com.bonborunote.niconicoviewer.network.NicoNicoApi.Field
import com.bonborunote.niconicoviewer.network.NicoNicoApi.Field.TAG
import com.bonborunote.niconicoviewer.network.NicoNicoApi.Sort.VIEW_COUNT_DESC
import com.bonborunote.niconicoviewer.network.NicoNicoApi.Target.TITLE
import com.bonborunote.niconicoviewer.network.NicoNicoException
import com.bonborunote.niconicoviewer.network.response.Content
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

class SearchResultRepository(
    private val nicoNicoApi: NicoNicoApi
) {
  val loading = BehaviorSubject.createDefault(false)
  val error = BehaviorSubject.create<NicoNicoException>()
  val results = BehaviorSubject.create<List<Content>>()

  fun search(keyword: String) {
    async(CommonPool) {
      try {
        loading.onNext(true)
        val result = nicoNicoApi.search(
            keyword = keyword,
            targets = listOf(TITLE),
            sort = VIEW_COUNT_DESC,
            fields = listOf(Field.TITLE, TAG))
        results.onNext(result)
      } catch (e: NicoNicoException) {
        error.onNext(e)
      } catch (e: Exception) {
      } finally {
        loading.onNext(false)
      }
    }
  }
}
