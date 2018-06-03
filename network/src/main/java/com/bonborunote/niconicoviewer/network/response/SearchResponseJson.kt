package com.bonborunote.niconicoviewer.network.response

import com.google.gson.annotations.SerializedName

data class SearchResponseJson(
    @SerializedName("meta") val metaJson: MetaJson,
    @SerializedName("data") val data: List<ContentJson>
)

