package com.bonborunote.niconicoviewer.latest.usecase

import com.bonborunote.niconicoviewer.common.models.LatestVideo

interface LatestUseCase {
  fun getLatest(page: Int): List<LatestVideo>
}
