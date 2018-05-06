package com.bonborunote.niconicoviewer.detail.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bonborunote.niconicoviewer.common.models.Tag
import com.bonborunote.niconicoviewer.detail.ui.databinding.LayoutTagContainerBinding
import com.google.android.flexbox.FlexboxLayout
import com.xwray.groupie.Item
import com.xwray.groupie.databinding.BindableItem

class TagItem(
  context: Context,
  private val tags: List<Tag>,
  private val callback: (Tag) -> Unit
) : BindableItem<LayoutTagContainerBinding>() {

  private val layoutInflater = LayoutInflater.from(context)
  private val clickListener = View.OnClickListener {
    (it.tag as? Tag)?.let {
      callback(it)
    }
  }

  override fun getLayout(): Int {
    return R.layout.layout_tag_container
  }

  override fun bind(viewBinding: LayoutTagContainerBinding, position: Int) {
    viewBinding.flexbox.removeAllViews()
    tags.forEachIndexed { index, it ->
      val tag = layoutInflater.inflate(R.layout.layout_tag, viewBinding.flexbox, false)
      tag as TextView
      tag.tag = it
      tag.setOnClickListener(clickListener)
      tag.text = it.value
      val lp = tag.layoutParams as FlexboxLayout.LayoutParams
      lp.order = index
      tag.layoutParams = lp
      viewBinding.flexbox.addView(tag)
    }
  }

  override fun isSameAs(other: Item<*>?): Boolean {
    return other is TagItem
  }

  override fun equals(other: Any?): Boolean {
    if (other !is TagItem) return false
    return other.tags !== tags
  }
}
