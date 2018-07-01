package com.bonborunote.niconicoviewer.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.bonborunote.niconicoviewer.components.preferences.PreferenceViewModel
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val preferenceModule = Kodein.Module {

  bind<PreferenceViewModel>() with scoped(androidScope<Fragment>())
    .singleton {
      ViewModelProviders.of(context,
        PreferenceViewModel.Factory(instance()))
        .get(PreferenceViewModel::class.java)
    }
}
