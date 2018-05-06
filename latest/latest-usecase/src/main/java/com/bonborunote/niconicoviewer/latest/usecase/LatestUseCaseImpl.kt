package com.bonborunote.niconicoviewer.latest.usecase

import com.bonborunote.niconicoviewer.common.models.LatestVideo
import com.bonborunote.niconicoviewer.latest.domain.LatestVideoRepository

internal class LatestUseCaseImpl(
  private val repository: LatestVideoRepository
): LatestUseCase {
  override fun getLatest(): List<LatestVideo> {
    return repository.getLatestVideos()
  }
}

class LatestUseCaseFactory {
  fun build(latestVideoRepository: LatestVideoRepository): LatestUseCase {
    return LatestUseCaseImpl(latestVideoRepository)
  }
}
