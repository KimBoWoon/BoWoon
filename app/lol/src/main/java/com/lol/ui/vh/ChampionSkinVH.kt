package com.lol.ui.vh

import com.bowoon.lol.databinding.VhChampionSkinBinding
import com.domain.lol.dto.Skin
import com.lol.base.BaseVH
import com.data.util.Log

class ChampionSkinVH(
    override val binding: VhChampionSkinBinding
) : BaseVH<VhChampionSkinBinding, Skin>(binding){
    override fun bind(item: Skin?) {
        runCatching {
            item ?: run {
                Log.e("ChampionSkinVH item is null!")
                return
            }
        }.onSuccess { skinInfo ->
            binding.apply {
                skin = skinInfo
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}