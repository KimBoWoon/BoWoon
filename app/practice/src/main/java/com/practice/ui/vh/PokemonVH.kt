package com.practice.ui.vh

import com.domain.practice.dto.SealedPokemon
import com.practice.base.BaseVH
import com.practice.databinding.ViewholderPokemonBinding
import com.practice.ui.fragments.PokemonListFragment
import util.Log

class PokemonVH(
    override val binding: ViewholderPokemonBinding,
    private val clickHandler: PokemonListFragment.ClickHandler
) : BaseVH<ViewholderPokemonBinding, SealedPokemon.Pokemon>(binding) {
    override fun bind(item: SealedPokemon.Pokemon?) {
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

    fun goToDetail(pokemon: SealedPokemon.Pokemon) {
        clickHandler.goToDetail(pokemon, 0)
    }
}