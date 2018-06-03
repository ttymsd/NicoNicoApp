package com.bonborunote.niconicoviewer.latest.domain

import com.bonborunote.niconicoviewer.common.models.LatestVideo

interface LatestVideoRepository {
  fun getLatestVideos(page: Int): List<LatestVideo>
}
