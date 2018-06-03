package com.bonborunote.groupie.aac.plugin

import android.arch.paging.AsyncPagedListDiffer
import android.arch.paging.PagedList
import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.util.DiffUtil
import android.support.v7.util.ListUpdateCallback
import com.xwray.groupie.Group
import com.xwray.groupie.GroupDataObserver
import com.xwray.groupie.Item

 open class PagedSection<T : Item<*>> : Group, GroupDataObserver {
  private val observable = GroupDataObservable()

  private val listUpdateCallback: ListUpdateCallback = object : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {
      onItemRangeChanged(this@PagedSection, position, count)
    }

    override fun onMoved(fromPosition: Int, toPosition: Int) {
      onItemMoved(this@PagedSection, fromPosition, toPosition)
    }

    override fun onInserted(position: Int, count: Int) {
      onItemRangeInserted(this@PagedSection, position, count)
    }

    override fun onRemoved(position: Int, count: Int) {
      onItemRangeRemoved(this@PagedSection, position, count)
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

  fun submitList(pagedList: PagedList<T>) {
    differ.submitList(pagedList)
  }

  fun getGroupCount(): Int {
    return differ.currentList?.size ?: 0
  }

  fun getGroup(position: Int): Group {
    return differ.getItem(position) ?: throw IndexOutOfBoundsException()
  }

  fun getPosition(group: Group): Int {
    return differ.currentList?.indexOf(group) ?: -1
  }

  override fun registerGroupDataObserver(groupDataObserver: GroupDataObserver) {
    observable.registerObserver(groupDataObserver)
  }

  override fun unregisterGroupDataObserver(groupDataObserver: GroupDataObserver) {
    observable.unregisterObserver(groupDataObserver)
  }

  override fun getItemCount(): Int {
    return differ.currentList?.sumBy { it.itemCount } ?: 0
  }

  override fun getItem(position: Int): Item<*> {
    return differ.getItem(position) ?:
    throw IndexOutOfBoundsException(
        "Wanted item at $position but there are only $itemCount items")
  }

  override fun getPosition(item: Item<*>): Int {
    return differ.currentList?.indexOf(item) ?: -1
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

