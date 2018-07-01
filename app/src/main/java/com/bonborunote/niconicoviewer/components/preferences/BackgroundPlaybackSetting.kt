package com.bonborunote.niconicoviewer.components.preferences

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.CompoundButton
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutBackgroundPlaybackSettingBinding
import com.bonborunote.niconicoviewer.utils.checkOverlayPermission
import com.bonborunote.niconicoviewer.utils.showToast
import com.xwray.groupie.databinding.BindableItem

class BackgroundPlaybackSetting(
  private val isChecked: Boolean,
  private val callback: (changed: Boolean) -> Unit
) : BindableItem<LayoutBackgroundPlaybackSettingBinding>(), CompoundButton.OnCheckedChangeListener {
  override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
    updateCallback(buttonView, isChecked)
  }

  override fun getLayout(): Int {
    return R.layout.layout_background_playback_setting
  }

  override fun bind(viewBinding: LayoutBackgroundPlaybackSettingBinding, position: Int) {
    viewBinding.root.setOnClickListener {
      updateCallback(viewBinding.toggle, !isChecked)
    }
    viewBinding.toggle.setOnCheckedChangeListener(null)
    viewBinding.toggle.isChecked = isChecked
    viewBinding.toggle.setOnCheckedChangeListener(this)
  }

  private fun updateCallback(buttonView: CompoundButton, isChecked: Boolean) {
    if (isChecked && !buttonView.context.checkOverlayPermission()) {
      val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:${buttonView.context.packageName}"));
      buttonView.context.startActivity(intent)
      buttonView.context.showToast(R.string.need_overlay_permission)
      buttonView.setOnCheckedChangeListener(null)
      buttonView.isChecked = false
      buttonView.setOnCheckedChangeListener(this)
    } else {
      callback(isChecked)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is BackgroundPlaybackSetting) return false
    return other.isChecked == isChecked
      && callback == callback
  }

  override fun hashCode(): Int {
    return isChecked.hashCode() * 31 + callback.hashCode()
  }
}
