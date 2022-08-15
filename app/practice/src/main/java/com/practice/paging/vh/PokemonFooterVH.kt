package com.practice.paging.vh

import com.practice.base.BaseVH
import com.practice.databinding.ViewholderPokemonFooterBinding
import util.Log

class PokemonFooterVH(
    override val binding: ViewholderPokemonFooterBinding,
) : BaseVH<ViewholderPokemonFooterBinding, String>(binding) {
    override fun bind(item: String?) {
        runCatching {
            item ?: run {
                Log.e("PokemonFooterVH item is null!")
                return
            }
        }.onSuccess {
            binding.dto = it
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }
}