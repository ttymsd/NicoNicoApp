package com.bonborunote.niconicoviewer.common.utils

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

private val FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
  .withZone(ZoneId.of("Asia/Tokyo"))

fun convert(date: String): LocalDateTime {
  return LocalDateTime.parse(date, FORMATTER)
}
