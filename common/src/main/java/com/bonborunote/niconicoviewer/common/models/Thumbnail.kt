package com.bonborunote.niconicoviewer.common.models

private val THUMB_URL_REGEX = Regex("https?://tn\\.smilevideo\\.jp/smile\\?i=[0-9]+")
private val CONTENT_ID_FROM_REGEX = Regex("[0-9]+$")
private const val BASE_URL = "http://tn.smilevideo.jp/smile?i="
private const val MEDIUM_SUFFIX = ".M"
private const val LARGE_SUFFIX = ".L"

data class Thumbnail(
  val smallThumbnail: String,
  val mediumThumbnail: String,
  val largeThumbnail: String
)

fun String.fromDescription(): Thumbnail {
  val smallUrl = THUMB_URL_REGEX.findAll(this).firstOrNull()?.value ?: ""
  return Thumbnail(
    smallUrl,
    "$smallUrl$MEDIUM_SUFFIX",
    "$smallUrl$LARGE_SUFFIX"
  )
}

fun ContentId.thumbnailFromId(): Thumbnail {
  val imageId: String = CONTENT_ID_FROM_REGEX.findAll(value).firstOrNull()?.value ?: ""
  return Thumbnail(
    "$BASE_URL$imageId",
    "$BASE_URL$imageId$MEDIUM_SUFFIX",
    "$BASE_URL$imageId$LARGE_SUFFIX"
  )
}


