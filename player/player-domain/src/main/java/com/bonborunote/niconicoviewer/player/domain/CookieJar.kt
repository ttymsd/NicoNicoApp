package com.bonborunote.niconicoviewer.components.player

import android.webkit.CookieManager
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.Collections.emptyList

class CookieJar : CookieJar {
  private val cookieManager = CookieManager.getInstance()

  override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
    val urlString = url.toString()

    for (cookie in cookies) {
      cookieManager.setCookie(urlString, cookie.toString())
    }
  }

  override fun loadForRequest(url: HttpUrl?): MutableList<Cookie> {
    val urlString = url.toString()
    val cookiesString = cookieManager.getCookie(urlString)

    if (cookiesString != null && !cookiesString.isEmpty()) {
      //We can split on the ';' char as the cookie manager only returns cookies
      //that match the url and haven't expired, so the cookie attributes aren't included
      val cookieHeaders = cookiesString.split(";".toRegex())
          .dropLastWhile({ it.isEmpty() }).toTypedArray()
      return cookieHeaders.mapNotNull { Cookie.parse(url, it) }.toMutableList()
    }
    return emptyList()
  }
}
