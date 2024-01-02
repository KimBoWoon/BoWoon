package com.bowoon.lol.ui.vh

import com.bowoon.commonutils.Log
import com.bowoon.lol.base.BaseVH
import com.bowoon.lol.data.Passive
import com.bowoon.lol.databinding.VhChampionPassiveBinding

class ChampionPassiveVH(
    override val binding: VhChampionPassiveBinding
) : BaseVH<VhChampionPassiveBinding, Passive>(binding) {
    override fun bind(item: Passive?) {
        runCatching {
            item ?: run {
                Log.e("ChampionPassiveVH item is null")
                return
            }
        }.onSuccess { passiveInfo ->
            binding.apply {
                passive = passiveInfo
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}