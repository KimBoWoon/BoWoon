package com.bowoon.lol.ui.vh

import com.bowoon.commonutils.Log
import com.bowoon.lol.base.BaseVH
import com.bowoon.lol.data.Skin
import com.bowoon.lol.databinding.VhChampionSkinBinding

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