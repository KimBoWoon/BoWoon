package com.practice.ui.vh

import com.domain.practice.dto.PokemonModel
import com.practice.base.BaseVH
import com.practice.databinding.ViewholderPokemonBinding
import com.practice.ui.fragments.PokemonListFragment
import util.Log

class PokemonVH(
    override val binding: ViewholderPokemonBinding,
    private val clickHandler: PokemonListFragment.ClickHandler
) : BaseVH<ViewholderPokemonBinding, PokemonModel.Pokemon>(binding) {
    override fun bind(item: PokemonModel.Pokemon?) {
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

    fun goToDetail(pokemon: PokemonModel.Pokemon) {
        clickHandler.goToDetail(pokemon, 0)
    }
}