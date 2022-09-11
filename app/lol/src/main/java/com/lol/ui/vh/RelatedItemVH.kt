package com.lol.ui.vh

import com.domain.lol.dto.GameItemInfo
import com.lol.base.BaseVH
import com.lol.databinding.VhRelatedItemBinding
import util.Log

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