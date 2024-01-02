package com.bowoon.practice.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseVH<V : ViewDataBinding, I : Any>(
    protected open val binding: V
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: I?)
}