package com.bonborunote.niconicoviewer.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.bonborunote.niconicoviewer.detail.ui.DetailViewModel
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val detailModule = Kodein.Module {
  bind<DetailViewModel>() with scoped(androidScope<Fragment>())
      .singleton {
        ViewModelProviders.of(context, DetailViewModel.Factory(instance()))
            .get(DetailViewModel::class.java)
      }
}