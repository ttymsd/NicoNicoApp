package com.bonborunote.niconicoviewer.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.bonborunote.niconicoviewer.components.detail.DetailViewModel
import com.bonborunote.niconicoviewer.detail.domain.ContentDetailRepository
import com.bonborunote.niconicoviewer.detail.infra.ContentDetailRepositoryImpl
import com.bonboruntoe.niconicoviewer.detail.usecase.DetailUseCase
import com.bonboruntoe.niconicoviewer.detail.usecase.DetailUseCaseFactory
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val detailModule = Kodein.Module {
  bind<ContentDetailRepository>() with scoped(androidScope<FragmentActivity>())
    .singleton {
      ContentDetailRepositoryImpl(instance(), instance("video_rss"), instance("channel_rss"))
    }
  bind<DetailUseCase>() with scoped(androidScope<FragmentActivity>())
    .singleton {
      DetailUseCaseFactory().create(instance())
    }
  bind<DetailViewModel>() with scoped(androidScope<FragmentActivity>())
    .singleton {
      ViewModelProviders.of(context, DetailViewModel.Factory(instance()))
        .get(DetailViewModel::class.java)
    }
}