package com.lol.ui.vh

import com.domain.lol.dto.Spell
import com.lol.base.BaseVH
import com.lol.databinding.VhChampionSpellBinding
import util.Log

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