package com.bowoon.practice.ui.vh

import androidx.core.view.isVisible
import androidx.paging.LoadState
import com.bowoon.commonutils.Log
import com.bowoon.practice.base.BaseVH
import com.bowoon.practice.databinding.ViewholderLoadBinding

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
            binding.apply {
                vh = this@LoadStateVH
                tvLoadText.isVisible = loadState is LoadState.Loading
                pbNextLoading.isVisible = loadState is LoadState.Loading
                bRetry.isVisible = loadState is LoadState.Error
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun retry() {
        retry?.invoke()
    }
}