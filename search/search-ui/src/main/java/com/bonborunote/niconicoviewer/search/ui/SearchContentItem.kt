package com.bonborunote.niconicoviewer.search.ui

import android.view.View
import com.bonborunote.niconicoviewer.search.R
import com.bonborunote.niconicoviewer.search.databinding.LayoutSearchBinding
import com.bonborunote.niconicoviewer.search.domain.Content
import com.xwray.groupie.databinding.BindableItem
import timber.log.Timber

data class SearchContentItem(
    private val content: Content,
    private val listener: (content: Content) -> Unit
) : BindableItem<LayoutSearchBinding>(), View.OnClickListener {
  override fun bind(viewBinding: LayoutSearchBinding, position: Int) {
    viewBinding.content = content
    viewBinding.executePendingBindings()
    viewBinding.root.setOnClickListener(this)
  }

  override fun getLayout(): Int = R.layout.layout_search

  override fun onClick(v: View?) {
    Timber.d("$content")
    listener(content)
  }
}
