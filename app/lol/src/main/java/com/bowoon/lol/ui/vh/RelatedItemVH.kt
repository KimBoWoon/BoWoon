package com.bowoon.lol.ui.vh

import com.bowoon.commonutils.Log
import com.bowoon.lol.base.BaseVH
import com.bowoon.lol.data.GameItemInfo
import com.bowoon.lol.databinding.VhRelatedItemBinding

class RelatedItemVH(
    override val binding: VhRelatedItemBinding
) : BaseVH<VhRelatedItemBinding, GameItemInfo>(binding) {
    override fun bind(item: GameItemInfo?) {
        runCatching {
            item ?: run {
                Log.e("RelatedItemVH item is null!")
                return
            }
        }.onSuccess {
            binding.apply {
                vh = this@RelatedItemVH
                gameItem = item
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}