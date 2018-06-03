package com.bonborunote.niconicoviewer.modules

import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import com.bonborunote.niconicoviewer.components.search.SearchViewModel
import com.bonborunote.niconicoviewer.search.domain.ContentRepository
import com.bonborunote.niconicoviewer.search.infra.ContentRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.android.androidScope
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.scoped
import org.kodein.di.generic.singleton

val searchModule = Kodein.Module {
  bind<ContentRepository>() with scoped(androidScope<FragmentActivity>())
    .singleton {
      ContentRepositoryImpl(instance())
    }
  bind<SearchViewModel>() with scoped(androidScope<FragmentActivity>())
    .singleton {
      ViewModelProviders.of(context,
        SearchViewModel.Factory(instance(), instance()))
        .get(SearchViewModel::class.java)
    }
}
