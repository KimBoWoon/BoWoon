package com.lol.ui.vh

import com.lol.base.BaseVH
import com.lol.databinding.VhEmptyBinding
import util.Log

class EmptyVH(
    override val binding: VhEmptyBinding
) : BaseVH<VhEmptyBinding, Any>(binding) {
    override fun bind(item: Any?) {
        runCatching {
            item ?: run {
                Log.e("EmptyVH item is null")
                return
            }
        }.onSuccess {
            Log.d("create EmptyVH")
        }.onFailure { e ->
            Log.d("error EmptyVH >>>>> ${e.message}")
        }
    }
}