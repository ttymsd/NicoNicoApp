package com.bonborunote.niconicoviewer.domain.network

import com.bonborunote.niconicoviewer.domain.network.models.SearchResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

abstract class NicoNicoApi(
    retrofit: Retrofit
) {
  protected val api = retrofit.create(Service::class.java)

  abstract fun search(keyword: String,
      targets: String,
      sort: Sort,
      fields: List<Target> = emptyList(),
      filters: List<Field> = emptyList(),
      jsonFilters: String? = null,
      offset: Int = 0,
      limit: Int = 10,
      context: String? = null): SearchResponse

  interface Service {
    @GET("/api/v2/snapshot/video/contents/search")
    fun search(@Query("q") keyword: String,
        @Query("targets") targets: String,
        @Query("_sort") sort: String,
        @Query("fields") fields: String? = null,
        @Query("filters") filters: String? = null,
        @Query("jsonFilter") jsonFilters: String? = null,
        @Query("_offset") offset: Int = 0,
        @Query("_limit") limit: Int = 10,
        @Query("_context") context: String? = null): Call<SearchResponse>
  }

  enum class Sort(val key: String) {
    VIEW_COUNT_ASC("+viewCounter"),
    VIEW_COUNT_DESC("-viewCounter"),
    MYLIST_COUNT_ASC("+viewCounter"),
    MYLIST_COUNT_DESC("-viewCounter"),
    COMMENT_COUNT_ASC("+commentCounter"),
    COMMENT_COUNT_DESC("-commentCounter"),
    START_TIME_COUNT_ASC("+startTime"),
    START_TIME_COUNT_DESC("-startTime"),
    LAST_COMMENT_TIME_COUNT_ASC("+lastCommentTime"),
    LAST_COMMENT_TIME_COUNT_DESC("-lastCommentTime"),
    LENGTH_SECONDS_COUNT_ASC("+lengthSeconds"),
    LENGTH_SECONDS_COUNT_DESC("-lengthSeconds")
  }

  enum class Target(val key: String) {
    TITLE("title"),
    DESCRIPTION("description"),
    TAGS("tags")
  }

  enum class Field(val key: String) {
    CONTENT_ID("contentId"),
    TITLE("title"),
    DESCRIPTION("description"),
    TAG("tag")
  }
}

