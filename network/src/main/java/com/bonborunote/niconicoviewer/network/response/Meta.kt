package com.bonborunote.niconicoviewer.network.response

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("status") val status: Int,
    @SerializedName("errorCode") val errorCode: String,
    @SerializedName("errorMessage") val errorMessage: String
)

