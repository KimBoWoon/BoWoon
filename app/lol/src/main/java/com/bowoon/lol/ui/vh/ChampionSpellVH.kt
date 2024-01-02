package com.bowoon.lol.ui.vh

import com.bowoon.commonutils.Log
import com.bowoon.lol.base.BaseVH
import com.bowoon.lol.data.Spell
import com.bowoon.lol.databinding.VhChampionSpellBinding

class ChampionSpellVH(
    override val binding: VhChampionSpellBinding
) : BaseVH<VhChampionSpellBinding, Spell>(binding) {
    override fun bind(item: Spell?) {
        runCatching {
            item ?: run {
                Log.e("ChampionSpellVH item is null!")
                return
            }
        }.onSuccess { spellInfo ->
            binding.apply {
                spell = spellInfo
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}