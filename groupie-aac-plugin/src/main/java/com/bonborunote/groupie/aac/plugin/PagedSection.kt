package com.bonborunote.groupie.aac.plugin

import android.arch.paging.PagedList
import android.support.v7.util.DiffUtil
import com.xwray.groupie.Group
import com.xwray.groupie.Item

class PagedSection<T: Item<*>>(
    groups: List<Group> = emptyList(),
    header: Group? = null
) : PagedListNestedGroup<T>() {

  private var header: Group? = null
  private val children: MutableList<Group> = arrayListOf()
  private var footer: Group? = null
  private val placeholder: Group? = null
  private var isHeaderAndFooterVisible: Boolean = false
  private var hideWhenEmpty: Boolean = false
  private var isPlaceholderVisible: Boolean = false

  init {
    this.header = header
    addAll(groups)
  }

  override fun getGroup(position: Int): Group {
    var p = position
    if (isHeaderShown() && p == 0) return header!!
    p -= getHeaderCount()
    if (isPlaceHolderShown() && p == 0) return placeholder!!
    p -= getPlaceholderCount()
    if (p == children.size) {
      if (isFooterShown()) {
        return footer!!
      } else {
        throw IndexOutOfBoundsException()
      }
    } else {
      return children[p]
    }
  }

  override fun getGroupCount(): Int {
    return getHeaderCount() + getFooterCount() + getPlaceholderCount() + children.size
  }

  private fun isHeaderShown(): Boolean {
    return getHeaderCount() > 0
  }

  private fun isPlaceHolderShown(): Boolean {
    return getPlaceholderItemCount() > 0
  }

  override fun submitList(pagedList: PagedList<T>) {
    super.submitList(pagedList)
    addAll(pagedList)
  }

  override fun add(group: Group) {
    super.add(group)
    val position = getItemCountWithoutFooter()
    children.add(group)
    notifyItemRangeInserted(position, group.itemCount)
    refreshEmptyState()
  }

  override fun add(position: Int, group: Group) {
    super.add(position, group)
    children.add(position, group)
    val notifyPosition = getHeaderItemCount() + getItemCount(children.subList(0, position))
    notifyItemRangeInserted(notifyPosition, group.itemCount)
    refreshEmptyState()
  }

  override fun addAll(groups: List<Group>) {
    if (groups.isEmpty()) return
    super.addAll(groups)
    val position = getItemCountWithoutFooter()
    children.addAll(groups)
    notifyItemRangeInserted(position, getItemCount(groups))
    refreshEmptyState()
  }

  override fun addAll(position: Int, groups: List<Group>) {
    if (groups.isEmpty()) return
    super.addAll(position, groups)
    children.addAll(position, groups)
    val notifyPosition = getHeaderItemCount() + getItemCount(children.subList(0, position))
    notifyItemRangeInserted(notifyPosition, getItemCount(groups))
    refreshEmptyState()
  }

  override fun remove(group: Group) {
    super.remove(group)
    val position = getItemCountBeforeGroup(group)
    children.remove(group)
    notifyItemRangeRemoved(position, group.itemCount)
    refreshEmptyState()
  }

  override fun removeAll(groups: List<Group>) {
    if (groups.isEmpty()) return
    super.removeAll(groups)
    groups.forEach {
      val position = getItemCountBeforeGroup(it)
      children.remove(it)
      notifyItemRangeRemoved(position, it.itemCount)
    }
    refreshEmptyState()
  }

  fun update(groups: List<Group>) {
    val section = PagedSection<T>(groups)


    val headerItemCount = getHeaderItemCount()
    val oldBodyItemCount = getItemCount(children)
    val newBodyItemCount = section.itemCount

    val diffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
      override fun getOldListSize(): Int {
        return oldBodyItemCount
      }

      override fun getNewListSize(): Int {
        return newBodyItemCount
      }

      override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getItem(headerItemCount + oldItemPosition)
        val newItem = section.getItem(newItemPosition)
        return newItem.isSameAs(oldItem)
      }

      override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = getItem(headerItemCount + oldItemPosition)
        val newItem = section.getItem(newItemPosition)
        return newItem == oldItem
      }
    })

    super.removeAll(children)
    children.clear()
    children.addAll(groups)
    super.addAll(groups)
    diffResult.dispatchUpdatesTo(listUpdateCallback)
    refreshEmptyState()
  }

  fun getItemCount(groups: List<Group>): Int {
    return groups.sumBy { it.itemCount }
  }

  fun getItemCountBeforeGroup(group: Group): Int {
    return getItemCountBeforeGroup(getPosition(group))
  }

  fun getItemCountBeforeGroup(groupIndex: Int): Int {
    return (0 until groupIndex).sumBy { getGroup(it).itemCount }
  }

  override fun getPosition(group: Group): Int {
    var count = 0
    if (isHeaderShown()) {
      if (group === header) return count
    }
    count += getHeaderCount()
    if (isPlaceholderShown()) {
      if (group === placeholder) return count
    }
    count += getPlaceholderCount()

    val index = children.indexOf(group)
    if (index >= 0) return count + index
    count += children.size

    if (isFooterShown()) {
      if (footer === group) {
        return count
      }
    }

    return -1
  }

  private fun refreshEmptyState() {
    if (isEmpty()) {
      if (hideWhenEmpty) {
        hideDecorations()
      } else {
        showPlaceholder()
        showHeadersAndFooters()
      }
    } else {
      hidePlaceholder()
      showHeadersAndFooters()
    }
  }

  private fun hideDecorations() {
    if (!isHeaderAndFooterVisible && !isPlaceholderVisible) return

    val count = getHeaderItemCount() + getPlaceholderItemCount() + getFooterItemCount()
    isHeaderAndFooterVisible = false
    isPlaceholderVisible = false
    notifyItemRangeRemoved(0, count)
  }

  private fun getPlaceholderItemCount(): Int {
    return if (isPlaceholderVisible && placeholder != null) {
      return placeholder.itemCount
    } else {
      0
    }
  }

  private fun getFooterItemCount(): Int {
    return if (getFooterCount() == 0) 0 else footer?.itemCount ?: 0
  }

  private fun showHeadersAndFooters() {
    if (isHeaderAndFooterVisible) return

    isHeaderAndFooterVisible = true
    notifyItemRangeInserted(0, getHeaderItemCount())
    notifyItemRangeInserted(getItemCountWithoutFooter(), getFooterItemCount())
  }

  private fun getItemCountWithoutFooter(): Int {
    return getBodyItemCount() + getHeaderItemCount()
  }

  private fun getBodyItemCount(): Int {
    return if (isPlaceholderVisible) getPlaceholderItemCount() else getItemCount(children)
  }

  private fun showPlaceholder() {
    if (isPlaceholderVisible || placeholder == null) return
    isPlaceholderVisible = true
    notifyItemRangeInserted(getHeaderItemCount(), placeholder.itemCount)
  }

  private fun hidePlaceholder() {
    if (!isPlaceholderVisible || placeholder == null) return
    isPlaceholderVisible = false
    notifyItemRangeRemoved(getHeaderItemCount(), placeholder.itemCount)
  }

  private fun isEmpty(): Boolean {
    return children.isEmpty() || getItemCount(children) == 0
  }

  private fun getHeaderItemCount(): Int {
    return if (getHeaderCount() == 0) 0 else header?.itemCount ?: 0
  }

  private fun getHeaderCount(): Int {
    return if (header == null || !isHeaderAndFooterVisible) 0 else 1
  }

  private fun getPlaceholderCount(): Int {
    return if (placeholder == null || !isPlaceholderVisible) 0 else 1
  }

  private fun isFooterShown(): Boolean {
    return getFooterCount() > 0
  }

  private fun getFooterCount(): Int {
    return if (footer == null || !isHeaderAndFooterVisible) 0 else 1
  }

  private fun isPlaceholderShown(): Boolean {
    return getPlaceholderCount() > 0
  }

  fun setFooter(footer: Group?) {
    if (footer == null)
      throw NullPointerException("Footer can't be null.  Please use removeFooter() instead!")
    val previousFooterItemCount = getFooterItemCount()
    this.footer = footer
    notifyFooterItemsChanged(previousFooterItemCount)
  }

  fun removeFooter() {
    val previousFooterItemCount = getFooterItemCount()
    this.footer = null
    notifyFooterItemsChanged(previousFooterItemCount)
  }

  private fun notifyFooterItemsChanged(previousFooterItemCount: Int) {
    val newFooterItemCount = getFooterItemCount()
    if (previousFooterItemCount > 0) {
      notifyItemRangeRemoved(getItemCountWithoutFooter(), previousFooterItemCount)
    }
    if (newFooterItemCount > 0) {
      notifyItemRangeInserted(getItemCountWithoutFooter(), newFooterItemCount)
    }
  }
}
