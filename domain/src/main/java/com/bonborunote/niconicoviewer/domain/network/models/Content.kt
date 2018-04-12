package com.bonborunote.niconicoviewer.domain.network.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Content(
    @SerializedName("contentId") val contentId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("categoryTags") val categoryTags: String?,
    @SerializedName("viewCounter") val viewCounter: Int,
    @SerializedName("mylistCounter") val mylistCounter: Int,
    @SerializedName("commentCounter") val commentCounter: Int,
    @SerializedName("startTime") val startTime: Date,
    @SerializedName("lastCommentTime") val lastCommentTime: Int,
    @SerializedName("lengthSeconds") val lengthSeconds: Int
)

