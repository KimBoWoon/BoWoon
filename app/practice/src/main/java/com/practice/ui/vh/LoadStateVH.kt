package com.practice.ui.vh

import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.practice.base.BaseVH
import com.practice.databinding.ViewholderLoadBinding
import util.Log

class LoadStateVH(
    override val binding: ViewholderLoadBinding,
    private val retry: (() -> Unit)? = null
) : BaseVH<ViewholderLoadBinding, LoadState>(binding) {
    override fun bind(item: LoadState?) {
        runCatching {
            item ?: run {
                Log.e("LoadStateViewHolder item is null!")
                return
            }
        }.onSuccess { loadState ->
            binding.tvLoadText.isVisible = loadState is LoadState.Loading
            binding.pbNextLoading.isVisible = loadState is LoadState.Loading
            binding.bRetry.isVisible = loadState is LoadState.Error
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun retry() {
        retry?.invoke()
    }
}