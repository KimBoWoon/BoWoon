package com.bowoon.lol.ui.vh

import com.bowoon.commonutils.Log
import com.bowoon.lol.base.BaseVH
import com.bowoon.lol.databinding.VhEmptyBinding

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