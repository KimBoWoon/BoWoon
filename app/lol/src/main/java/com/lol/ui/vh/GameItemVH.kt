package com.lol.ui.vh

import com.domain.lol.dto.GameItemInfo
import com.lol.base.BaseVH
import com.lol.databinding.VhGameItemBinding
import util.Log

class GameItemVH(
    override val binding: VhGameItemBinding,
    private val version: String? = null
) : BaseVH<VhGameItemBinding, GameItemInfo>(binding) {
    override fun bind(item: GameItemInfo?) {
        runCatching {
            item ?: run {
                Log.e("GameItemVH item is null!")
                return
            }
            version ?: run {
                Log.e("GameItemVH version is null!")
                return
            }
            Pair(item, version)
        }.onSuccess { (gameItemInfo, version) ->
            binding.version = version
            binding.gameItem = gameItemInfo
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}