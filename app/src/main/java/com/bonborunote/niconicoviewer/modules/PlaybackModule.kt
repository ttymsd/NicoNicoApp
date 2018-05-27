package com.bonborunote.niconicoviewer.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.bonborunote.niconicoviewer.components.playback.PlaybackViewModel
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val playbackModule = Kodein.Module {
  bind<PlaybackViewModel>() with scoped(androidScope<FragmentActivity>())
    .singleton {
      ViewModelProviders.of(context,
        PlaybackViewModel.Factory(context, instance()))
        .get(PlaybackViewModel::class.java)
    }
}
