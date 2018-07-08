package com.bonborunote.niconicoviewer.utils

import android.content.Context
import android.graphics.Rect
import android.support.annotation.DimenRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.view.View
import android.support.v7.widget.RecyclerView.State

class ListViewMarginDecorator(
  private val marginTop: Int
) : ItemDecoration() {
  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
    super.getItemOffsets(outRect, view, parent, state)
    val index = parent.getChildAdapterPosition(view)
    when (index) {
      0, parent.adapter.itemCount -> Unit
      else -> {
        outRect.top = marginTop
      }
    }
  }

  companion object {
    fun create(context: Context?, @DimenRes resId: Int): ListViewMarginDecorator {
      return ListViewMarginDecorator(context?.resources?.getDimensionPixelOffset(resId) ?: 0)
    }
  }
}
