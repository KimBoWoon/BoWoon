package com.practice.ui.vh

import com.practice.base.BaseVH
import com.practice.databinding.ViewholderPokemonHeaderBinding
import util.Log

class PokemonHeaderVH(
    override val binding: ViewholderPokemonHeaderBinding,
) : BaseVH<ViewholderPokemonHeaderBinding, String>(binding) {
    override fun bind(item: String?) {
        runCatching {
            item ?: run {
                Log.e("PokemonHeaderViewHolder item is null!")
                return
            }
        }.onSuccess {
            binding.dto = it
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}