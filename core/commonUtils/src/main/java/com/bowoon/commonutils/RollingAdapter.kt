package com.bowoon.commonutils

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class RollingAdapter<T, V : RecyclerView.ViewHolder>(
    private val diffUtil: DiffUtil.ItemCallback<T>
) : ListAdapter<T, V>(diffUtil) {
    companion object {
        private const val TAG = "common_utils_rolling_adapter"
    }

    override fun submitList(list: List<T>?) {
        list?.let {
            mutableListOf<T>().apply {
                add(list.last())
                addAll(list)
                add(list.first())
            }
        }.run {
            super.submitList(this)
        }
    }

    override fun submitList(list: List<T>?, commitCallback: Runnable?) {
        list?.let {
            mutableListOf<T>().apply {
                add(list.last())
                addAll(list)
                add(list.first())
            }
        }.run {
            super.submitList(this, commitCallback)
        }
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V

    abstract override fun onBindViewHolder(holder: V, position: Int)
}