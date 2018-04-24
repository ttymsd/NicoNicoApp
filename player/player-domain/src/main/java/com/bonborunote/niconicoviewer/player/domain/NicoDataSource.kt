package com.bonborunote.niconicoviewer.player.domain

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.util.Util
import okhttp3.OkHttpClient
import timber.log.Timber

class NicoDataSource(
    private val bufferSize: Long,
    defaultDataSourceFactory: DataSource.Factory
) : DataSource {

  private val defaultDataSource = defaultDataSourceFactory.createDataSource()

  override fun open(dataSpec: DataSpec): Long {
    Timber.d("open:$dataSpec")
    val d = DataSpec(dataSpec.uri,
        dataSpec.postBody,
        dataSpec.absoluteStreamPosition,
        dataSpec.position,
        dataSpec.length + bufferSize,
        dataSpec.key,
        dataSpec.flags)
    val result = defaultDataSource.open(d)
    Timber.d("opened:$result")
    return result
  }

  override fun getUri(): Uri {
    return defaultDataSource.uri
  }

  override fun close() {
    defaultDataSource.close()
  }

  override fun read(buffer: ByteArray?, offset: Int, readLength: Int): Int {
    Timber.d("read:$offset:$readLength")
    val result = defaultDataSource.read(buffer, offset, readLength)
    Timber.d("read:$result")
    return result
  }

  class Factory(
      context: Context?,
      httpClient: OkHttpClient,
      private var bufferSize: Long = DEFAULT_BUFFER_SIZE
  ) : DataSource.Factory {


    private val userAgent = Util.getUserAgent(context, "ExoPlayer")
    private val defaultDataSourceFactory = OkHttpDataSourceFactory(httpClient, userAgent, null)

    override fun createDataSource(): DataSource {
      return NicoDataSource(bufferSize, defaultDataSourceFactory)
    }
  }

  companion object {
    private val DEFAULT_BUFFER_SIZE = 1024L * 1024L
  }
}
