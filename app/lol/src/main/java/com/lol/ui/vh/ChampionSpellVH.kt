package com.lol.ui.vh

import com.domain.lol.dto.Spell
import com.lol.base.BaseVH
import com.lol.databinding.VhChampionSpellBinding
import util.Log

class ChampionSpellVH(
    override val binding: VhChampionSpellBinding,
    private val lolVersion: String? = null
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
                version = lolVersion
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}