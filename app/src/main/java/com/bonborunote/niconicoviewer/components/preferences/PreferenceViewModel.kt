package com.bonborunote.niconicoviewer.components.preferences

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.bonborunote.niconicoviewer.Preference

class PreferenceViewModel(
  private val preference: Preference
) : ViewModel(), LifecycleObserver {

  private val backgroundPlaybackEnable = MutableLiveData<Boolean>()

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
    backgroundPlaybackEnable.observeForever {
      it ?: return@observeForever
      preference.updateBackgroundPlaybackEnable(it)
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
  fun onDestroy() {
  }

  fun observeBackgroundPlaybackEnable(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
    backgroundPlaybackEnable.observe(lifecycleOwner, observer)
  }

  fun backgroundPlaybackEnable() : Boolean {
    return preference.backgroundPlaybackEnable()
  }

  fun updateBackgroundPlaybackEnable(enable: Boolean) {
    backgroundPlaybackEnable.postValue(enable)
  }

  @Suppress("UNCHECKED_CAST")
  class Factory(
    private val preference: Preference
  ) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return PreferenceViewModel(preference) as? T
        ?: throw IllegalArgumentException()
    }
  }
}
