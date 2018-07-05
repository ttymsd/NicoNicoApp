package com.bonborunote.niconicoviewer.components.preferences

import android.widget.CompoundButton
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutPictureInPictureSettingBinding
import com.xwray.groupie.databinding.BindableItem

class PictureInPictureSetting(
  private val isChecked: Boolean,
  private val callback: (changed: Boolean) -> Unit
) : BindableItem<LayoutPictureInPictureSettingBinding>(), CompoundButton.OnCheckedChangeListener {
  override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
    updateCallback(buttonView, isChecked)
  }

  override fun getLayout(): Int {
    return R.layout.layout_picture_in_picture_setting
  }

  override fun bind(viewBinding: LayoutPictureInPictureSettingBinding, position: Int) {
    viewBinding.root.setOnClickListener {
      updateCallback(viewBinding.toggle, !isChecked)
    }
    viewBinding.toggle.setOnCheckedChangeListener(null)
    viewBinding.toggle.isChecked = isChecked
    viewBinding.toggle.setOnCheckedChangeListener(this)
  }

  private fun updateCallback(buttonView: CompoundButton, isChecked: Boolean) {
    callback(isChecked)
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is PictureInPictureSetting) return false
    return other.isChecked == isChecked
      && callback == callback
  }

  override fun hashCode(): Int {
    return isChecked.hashCode() * 31 + callback.hashCode()
  }
}
