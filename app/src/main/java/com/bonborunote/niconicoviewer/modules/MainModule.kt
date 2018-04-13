package com.bonborunote.niconicoviewer.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.bonborunote.niconicoviewer.components.MainViewModel
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val mainModule = Kodein.Module {
  bind<MainViewModel>() with scoped(androidScope<FragmentActivity>())
      .singleton {
        ViewModelProviders.of(context, MainViewModel.Factory()).get(MainViewModel::class.java)
      }
}
