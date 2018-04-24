package com.bonborunote.niconicoviewer.player.domain

import android.content.Context
import android.util.AttributeSet
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bonborunote.niconicoviewer.components.player.MediaUrlSource
import kotlin.properties.Delegates

class JavaScriptEngine(context: Context, attributeSet: AttributeSet? = null,
    styleDef: Int = 0) : WebView(context, attributeSet, styleDef), MediaUrlSource {

  init {
    CookieManager.getInstance().setAcceptCookie(true)
    setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
    settings.apply {
      javaScriptEnabled = true
    }
    webChromeClient = WebChromeClient()
    webViewClient = NicoWebViewClient()
    addJavascriptInterface(JavaScriptInterface(), "Android")
  }

  private var mediaUrl: String by Delegates.observable("") { _, _, newValue ->
    find(newValue)
  }

  private var find: (mediaUrl: String) -> Unit = {}

  override fun findMediaUrl(contentId: String, callback: (mediaUrl: String) -> Unit) {
    find = callback
    loadUrl("http://www.nicovideo.jp/watch/$contentId")
  }

  inner class NicoWebViewClient : WebViewClient() {
    override fun onPageFinished(view: WebView, url: String) {
      super.onPageFinished(view, url)
      view.evaluateJavascript(START_JAVASCRIPT) {
        view.evaluateJavascript(STOP_VIDEO_JAVASCRIPT) {
          postDelayed({
            view.evaluateJavascript(FIND_VIDEO_SRC_JAVASCRIPT, null)
          }, 2000)
        }
      }
    }
  }

  inner class JavaScriptInterface {
    @JavascriptInterface fun findMediaUrl(url: String) {
      mediaUrl = url
    }
  }

  companion object {
    private const val START_JAVASCRIPT = "document.getElementsByClassName('watch-OpeningPlayerInfo_PlayButton')[0].click();"
    private const val STOP_VIDEO_JAVASCRIPT = "document.getElementsByClassName('start')[0].click();"
    private const val FIND_VIDEO_SRC_JAVASCRIPT = "Android.findMediaUrl(document.getElementsByTagName('video')[0].src)"
  }
}
