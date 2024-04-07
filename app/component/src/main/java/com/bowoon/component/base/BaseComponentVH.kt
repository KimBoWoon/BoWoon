package com.bowoon.component.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.component.data.Components

abstract class BaseComponentVH<out T : Components>(
    private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(component: @UnsafeVariance T?)
}