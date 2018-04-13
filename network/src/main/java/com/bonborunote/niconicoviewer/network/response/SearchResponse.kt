package com.bonborunote.niconicoviewer.network.response

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("meta") val meta: Meta,
    @SerializedName("data") val data: List<Content>
)

