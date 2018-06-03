package com.bonborunote.niconicoviewer.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.bonborunote.niconicoviewer.latest.domain.LatestVideoRepository
import com.bonborunote.niconicoviewer.latest.infra.LatestVideoRepoitoryImpl
import com.bonborunote.niconicoviewer.components.latest.LatestViewModel
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCase
import com.bonborunote.niconicoviewer.latest.usecase.LatestUseCaseFactory
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val latestModule = Kodein.Module {
  bind<LatestVideoRepository>() with scoped(androidScope<Fragment>())
    .singleton {
      LatestVideoRepoitoryImpl(instance("video_rss"))
    }
  bind<LatestUseCase>() with scoped(androidScope<Fragment>())
    .singleton {
      LatestUseCaseFactory().build(instance())
    }
  bind<LatestViewModel>() with scoped(androidScope<Fragment>())
    .singleton {
      ViewModelProviders.of(context, LatestViewModel.Factory(instance(), instance()))
        .get(LatestViewModel::class.java)
    }
}