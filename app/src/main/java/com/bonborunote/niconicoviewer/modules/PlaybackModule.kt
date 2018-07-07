package com.bonborunote.niconicoviewer.modules

import android.app.Service
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.bonborunote.niconicoviewer.components.background.BackgroundPlaybackViewModel
import com.bonborunote.niconicoviewer.components.playback.PlaybackViewModel
import com.bonborunote.niconicoviewer.player.infra.MediaUrlRepositoryFactory
import com.bonborunote.niconicoviewer.player.usecase.PlayerUseCase
import com.bonborunote.niconicoviewer.player.usecase.impl.PlayerUseCaseFactory
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val playbackModule = Kodein.Module {
  bind<PlayerUseCase>() with singleton {
    PlayerUseCaseFactory().build(
        instance(),
        instance(),
        MediaUrlRepositoryFactory(instance()).create())
  }

  bind<PlaybackViewModel>() with scoped(androidScope<FragmentActivity>())
      .singleton {
        ViewModelProviders.of(context, PlaybackViewModel.Factory(instance(), instance()))
            .get(PlaybackViewModel::class.java)
      }

  bind<BackgroundPlaybackViewModel>() with scoped(androidScope<Service>())
      .singleton { BackgroundPlaybackViewModel(instance()) }
}
