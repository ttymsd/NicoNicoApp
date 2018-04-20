package com.bonborunote.groupie.aac.plugin

import android.arch.paging.AsyncPagedListDiffer
import android.arch.paging.PagedList
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.xwray.groupie.Group
import com.xwray.groupie.Item
import com.xwray.groupie.NestedGroup

class PagedSection2<T : Item<*>>(
    groups: List<Group> = emptyList(),
    header: Group? = null
) : NestedGroup() {
  protected val listUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {
      this@PagedSection2.onItemRangeChanged(this@PagedSection2, position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
      this@PagedSection2.onItemMoved(this@PagedSection2, fromPosition, toPosition)
    }

    override fun onInserted(position: Int, count: Int) {
      this@PagedSection2.onItemRangeInserted(this@PagedSection2, position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
      this@PagedSection2.onItemRangeRemoved(this@PagedSection2, position, count)
    }
  }

  protected val diffCallback = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
      return oldItem.isSameAs(newItem)
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
      return oldItem == newItem
    }
  }

  private val differ = AsyncPagedListDiffer<T>(listUpdateCallback,
      AsyncDifferConfig.Builder(diffCallback).build())

  override fun getGroup(position: Int): Group {
    return differ.currentList?.get(position) ?: throw IndexOutOfBoundsException()
  }

  override fun getPosition(group: Group): Int {
    return differ.currentList?.indexOf(group) ?: -1
  }

  override fun getGroupCount(): Int {
    return differ.itemCount
  }

  fun submitList(pagedList: PagedList<T>) {
    differ.submitList(pagedList)
  }
}

