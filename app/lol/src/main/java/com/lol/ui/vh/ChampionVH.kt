package com.lol.ui.vh

import com.data.lol.util.Log
import com.domain.lol.dto.ChampionInfo
import com.lol.base.BaseVH
import com.lol.databinding.VhChampionBinding

class ChampionVH(
    override val binding: VhChampionBinding
) : BaseVH<VhChampionBinding, ChampionInfo>(binding) {
    override fun bind(item: ChampionInfo?) {
        runCatching {
            item ?: run {
                Log.e("ChampionVH item is null")
                return
            }
        }.onSuccess { championInfo ->
            binding.apply {
                vh = this@ChampionVH
                champion = championInfo
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun onClick(championInfo: ChampionInfo) {
        Log.d("${championInfo.name} clicked")
    }
}