package com.bonborunote.niconicoviewer.components.latest

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCase

class LatestDataSourceFactory(
  private val latestUseCase: LatestUseCase,
  private val clickCallback: (content: LatestVideo) -> Unit
) : DataSource.Factory<Int, LatestVideoItem>() {

  val dataSourceLiveData = MutableLiveData<LatestDataSource>()

  override fun create(): DataSource<Int, LatestVideoItem> {
    val dataSource = LatestDataSource(latestUseCase, clickCallback)
    dataSourceLiveData.postValue(dataSource)
    return dataSource
  }
}
