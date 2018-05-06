package com.bonborunote.niconicoviewer.latest.usecase

import com.bonborunote.niconicoviewer.common.models.LatestVideo

interface LatestUseCase {
  fun getLatest(): List<LatestVideo>
}
