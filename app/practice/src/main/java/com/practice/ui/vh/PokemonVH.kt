package com.practice.ui.vh

import com.bowoon.practice.databinding.ViewholderPokemonBinding
import com.domain.practice.dto.Pokemon
import com.practice.base.BaseVH
import com.practice.ui.fragments.PokemonListFragment
import util.Log

class PokemonVH(
    override val binding: ViewholderPokemonBinding,
    private val clickHandler: PokemonListFragment.ClickHandler
) : BaseVH<ViewholderPokemonBinding, Pokemon>(binding) {
    override fun bind(item: Pokemon?) {
        runCatching {
            item ?: run {
                Log.e("PokemonViewHolder item is null!")
                return
            }
        }.onSuccess { pokemon ->
            binding.apply {
                vh = this@PokemonVH
                dto = pokemon
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun goToDetail(pokemon: Pokemon) {
        clickHandler.goToDetail(pokemon, 0)
    }
}