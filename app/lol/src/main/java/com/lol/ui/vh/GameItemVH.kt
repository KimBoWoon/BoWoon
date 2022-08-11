package com.lol.ui.vh

import com.domain.lol.dto.GameItemInfo
import com.lol.base.BaseVH
import com.lol.databinding.VhGameItemBinding
import util.Log

class GameItemVH(
    override val binding: VhGameItemBinding
) : BaseVH<VhGameItemBinding, GameItemInfo>(binding) {
    override fun bind(item: GameItemInfo?) {
        runCatching {
            item ?: run {
                Log.e("GameItemVH item is null!")
                return
            }
        }.onSuccess { gameItemInfo ->
            binding.gameItem = gameItemInfo
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}