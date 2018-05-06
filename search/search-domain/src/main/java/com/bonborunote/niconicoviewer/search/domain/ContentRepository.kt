package com.bonborunote.niconicoviewer.search.domain

import com.bonborunote.niconicoviewer.common.models.Content
import com.bonborunote.niconicoviewer.search.domain.ContentRepository.Operator.AND
import com.bonborunote.niconicoviewer.search.domain.ContentRepository.Operator.EQUAL
import com.bonborunote.niconicoviewer.search.domain.ContentRepository.Operator.NOT
import com.bonborunote.niconicoviewer.search.domain.ContentRepository.Operator.OR
import com.bonborunote.niconicoviewer.search.domain.ContentRepository.Operator.RANGE
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

interface ContentRepository {
  fun search(keyword: String,
      sort: Sort,
      offset: Int,
      limit: Int,
      context: String? = null,
      jsonFilters: Filter? = null): List<Content>

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
