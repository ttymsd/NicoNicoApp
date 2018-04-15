package com.bonborunote.niconicoviewer.components.search

import android.util.Log
import android.view.View
import com.bonborunote.niconicoviewer.R
import com.bonborunote.niconicoviewer.databinding.LayoutSearchBinding
import com.bonborunote.niconicoviewer.network.response.Content
import com.xwray.groupie.databinding.BindableItem

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
    Log.d("AAA", "$content")
    listener(content)
  }
}
