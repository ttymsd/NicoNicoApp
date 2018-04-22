package com.bonborunote.niconicoviewer.network

import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Operator.AND
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Operator.EQUAL
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Operator.NOT
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Operator.OR
import com.bonborunote.niconicoviewer.network.NicoNicoSearchApi.Operator.RANGE
import com.bonborunote.niconicoviewer.network.response.Content
import com.bonborunote.niconicoviewer.network.response.SearchResponse
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

abstract class NicoNicoSearchApi(
    retrofit: Retrofit
) {
  protected val api = retrofit.create(
      Service::class.java)

  abstract fun search(keyword: String,
      targets: List<Target>,
      sort: Sort,
      fields: List<Field> = emptyList(),
      offset: Int = 0,
      limit: Int = 10,
      context: String? = null,
      jsonFilters: Filter? = null): List<Content>

  companion object {
    const val BASE_URL = "http://api.search.nicovideo.jp"
  }

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
    TAG("tags"),
    CATEGORY_TAG("categoryTags"),
    VIEW_COUNTER("viewCounter"),
    MYLIST_COUNTER("mylistCounter"),
    COMMENT_COUNTER("commentCounter"),
    START_TIME("startTime"),
    LAST_COMMENT_TIME("lastCommentTime"),
    LENGTH_SECONDS("lengthSeconds")
  }

  enum class FilterableField(val key: String) {
    CONTENT_ID("contentId"),
    TAG("tag"),
    CATEGORY_TAG("categoryTags"),
    VIEW_COUNTER("viewCounter"),
    MYLIST_COUNTER("mylistCounter"),
    COMMENT_COUNTER("commentCounter"),
    START_TIME("startTime"),
    LAST_COMMENT_TIME("lastCommentTime"),
    LENGTH_SECONDS("lengthSeconds")
  }

  enum class Operator(val key: String) {
    OR("or"),
    AND("and"),
    NOT("not"),
    EQUAL("equal"),
    RANGE("range")
  }

  data class Condition(
      @SerializedName("type") val type: String,
      @SerializedName("field") var field: String? = null,
      @SerializedName("value") var value: String? = null,
      @SerializedName("from") var from: String? = null,
      @SerializedName("to") var to: String? = null,
      @SerializedName("include_upper") var includeUpper: Boolean? = null,
      @SerializedName("include_lower") var includeLower: Boolean? = null
  ) {
    infix fun FilterableField.equal(value: String) {
      this@Condition.field = key
      this@Condition.value = value
    }

    infix fun FilterableField.from(value: String): Condition {
      this@Condition.field = key
      this@Condition.from = value
      this@Condition.includeLower = true
      return this@Condition
    }

    infix fun to(value: String) {
      this@Condition.to = value
      this@Condition.includeUpper = true
    }
  }

  data class Filter(
      @SerializedName("type") val type: String,
      @SerializedName("filters") val conditions: List<Condition>
  ) {
    override fun toString(): String {
      return Gson().toJson(this)
    }
  }

  fun or(setup: ArrayList<Condition>.() -> Unit): Filter {
    return Filter(OR.key, arrayListOf<Condition>().apply { setup() })
  }

  fun and(setup: ArrayList<Condition>.() -> Unit): Filter {
    return Filter(AND.key, arrayListOf<Condition>().apply { setup() })
  }

  fun not(setup: ArrayList<Condition>.() -> Unit): Filter {
    return Filter(NOT.key, arrayListOf<Condition>().apply { setup() })
  }

  fun ArrayList<Condition>.equal(setup: Condition.() -> Unit) {
    add(Condition(EQUAL.key).apply { setup() })
  }

  fun ArrayList<Condition>.range(setup: Condition.() -> Unit) {
    add(Condition(RANGE.key).apply { setup() })
  }
}

