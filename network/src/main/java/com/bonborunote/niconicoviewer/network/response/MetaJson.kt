package com.bonborunote.niconicoviewer.network.response

import com.google.gson.annotations.SerializedName

data class MetaJson(
    @SerializedName("status") val status: Int,
    @SerializedName("totalCount") val totalCount: Long,
    @SerializedName("errorCode") val errorCode: String,
    @SerializedName("errorMessage") val errorMessage: String
)

