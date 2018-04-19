package com.bonborunote.groupie.aac.plugin

import android.arch.paging.AsyncPagedListDiffer
import android.arch.paging.PagedList
import android.support.annotation.CallSuper
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import android.util.Log
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

abstract class PagedListNestedGroup<T : Item<*>> : Group, GroupDataObserver {
  private val observable = GroupDataObservable()

  protected val listUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {
      Log.d("OkHttp", "onChanged")
      onItemRangeChanged(this@PagedListNestedGroup, position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
      Log.d("OkHttp", "onMoved")
      onItemMoved(this@PagedListNestedGroup, fromPosition, toPosition)
    }

    override fun onInserted(position: Int, count: Int) {
      Log.d("OkHttp", "onInserted:${differ.currentList?.size}")
      onItemRangeInserted(this@PagedListNestedGroup, position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
      Log.d("OkHttp", "onRemoved")
      onItemRangeRemoved(this@PagedListNestedGroup, position, count)
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
      AsyncDifferConfig.Builder<T>(diffCallback).build())

  @CallSuper open fun submitList(pagedList: PagedList<T>) {
    differ.submitList(pagedList)
  }

  @CallSuper open fun add(group: Group) {
    group.registerGroupDataObserver(this)
  }

  @CallSuper open fun add(position: Int, group: Group) {
    group.registerGroupDataObserver(this)
  }

  @CallSuper open fun addAll(groups: List<Group>) {
    groups.forEach {
      it.registerGroupDataObserver(this)
    }
  }

  @CallSuper open fun addAll(position: Int, groups: List<Group>) {
    groups.forEach {
      it.registerGroupDataObserver(this)
    }
  }

  @CallSuper open fun remove(group: Group) {
    group.registerGroupDataObserver(this)
  }

  @CallSuper open fun removeAll(groups: List<Group>) {
    groups.forEach {
      it.unregisterGroupDataObserver(this)
    }
  }

  @CallSuper open fun notifyItemRangeInserted(positionStart: Int, itemCount: Int) {
    observable.onItemRangeInserted(this, positionStart, itemCount)
  }

  @CallSuper open fun notifyItemRangeRemoved(positionStart: Int, itemCount: Int) {
    observable.onItemRangeRemoved(this, positionStart, itemCount)
  }

  @CallSuper open fun notifyItemMoved(fromPosition: Int, toPosition: Int) {
    observable.onItemMoved(this, fromPosition, toPosition)
  }

  @CallSuper open fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
    observable.onItemRangeChanged(this, positionStart, itemCount)
  }

  abstract fun getGroup(position: Int): Group

  abstract fun getGroupCount(): Int

  abstract fun getPosition(group: Group): Int

  override fun getItemCount(): Int {
    return (0 until getGroupCount()).sumBy { getGroup(it).itemCount }
  }

  override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
    observable.registerObserver(groupDataObserver)
  }

  override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
    observable.unregisterObserver(groupDataObserver)
  }

  override fun getItem(position: Int): Item<*> {
    var previousPosition = 0
    (0 until getGroupCount()).forEach {
      val group = getGroup(it)
      val size = group.itemCount
      if (size + previousPosition > position) {
        return group.getItem(position - previousPosition)
      }
      previousPosition += size
    }

    throw IndexOutOfBoundsException(
        "Wanted item at $position but there are only $itemCount items")
  }

  override fun getPosition(item: Item<*>): Int {
    var previousPosition = 0
    (0 until getGroupCount()).forEach {
      val group = getGroup(it)
      val position = group.getPosition(item)
      if (position >= 0) {
        return position + previousPosition
      }
      previousPosition += group.itemCount
    }
    return -1
  }

  override fun onChanged(group: Group) {
    observable.onChanged(group)
  }

  override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
    observable.onItemRangeRemoved(group, positionStart, itemCount)
  }

  override fun onItemInserted(group: Group, position: Int) {
    observable.onItemInserted(group, position)
  }

  override fun onItemRemoved(group: Group, position: Int) {
    observable.onItemRemoved(group, position)
  }

  override fun onItemChanged(group: Group, position: Int) {
    observable.onItemChanged(group, position)
  }

  override fun onItemChanged(group: Group, position: Int, payload: Any?) {
    observable.onItemChanged(group, position, payload)
  }

  override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
    observable.onItemRangeInserted(group, positionStart, itemCount)
  }

  override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
    observable.onItemMoved(group, fromPosition, toPosition)
  }

  override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
    observable.onItemRangeChanged(group, positionStart, itemCount)
  }

  inner class GroupDataObservable : GroupDataObserver {
    private val observers: MutableList<GroupDataObserver> = arrayListOf()

    fun registerObserver(observer: GroupDataObserver) {
      synchronized(observers) {
        if (observers.contains(observer)) {
          throw IllegalStateException("Observer $observer is already registered.")
        }
        observers.add(observer)
      }
    }

    fun unregisterObserver(observer: GroupDataObserver) {
      synchronized(observers) {
        observers.remove(observer)
      }
    }

    override fun onChanged(group: Group) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onChanged(group)
      }
    }

    override fun onItemRangeRemoved(group: Group, positionStart: Int, itemCount: Int) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemRangeRemoved(group, positionStart, itemCount)
      }
    }

    override fun onItemInserted(group: Group, position: Int) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemInserted(group, position)
      }
    }

    override fun onItemRemoved(group: Group, position: Int) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemRemoved(group, position)
      }
    }

    override fun onItemChanged(group: Group, position: Int) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemChanged(group, position)
      }
    }

    override fun onItemChanged(group: Group, position: Int, payload: Any?) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemChanged(group, position, payload)
      }
    }

    override fun onItemRangeInserted(group: Group, positionStart: Int, itemCount: Int) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemRangeInserted(group, positionStart, itemCount)
      }
    }

    override fun onItemMoved(group: Group, fromPosition: Int, toPosition: Int) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemMoved(group, fromPosition, toPosition)
      }
    }

    override fun onItemRangeChanged(group: Group, positionStart: Int, itemCount: Int) {
      (observers.size - 1 downTo 0).forEach {
        observers[it].onItemRangeChanged(group, positionStart, itemCount)
      }
    }
  }
}

