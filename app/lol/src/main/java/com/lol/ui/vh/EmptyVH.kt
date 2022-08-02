package com.lol.ui.vh

import com.data.base.util.Log
import com.domain.lol.dto.ChampionInfo
import com.lol.base.BaseVH
import com.lol.databinding.VhEmptyBinding

class EmptyVH(
    override val binding: VhEmptyBinding
) : BaseVH<VhEmptyBinding, ChampionInfo>(binding) {
    override fun bind(item: ChampionInfo?) {
        runCatching {
            item ?: run {
                Log.e("EmptyVH item is null")
                return
            }
        }.onSuccess { champion ->
            Log.d("create EmptyVH >>>>> ${champion.name}")
        }.onFailure { e ->
            Log.d("error EmptyVH >>>>> ${e.message}")
        }
    }
}