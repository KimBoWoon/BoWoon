package com.practice.ui.vh

import com.domain.practice.dto.PokemonModel
import com.practice.base.BaseVH
import com.practice.databinding.ViewholderWishPokemonBinding
import com.practice.ui.fragments.WishPokemonList
import util.Log

class WishPokemonVH(
    override val binding: ViewholderWishPokemonBinding,
    private val clickHandler: WishPokemonList.ClickHandler? = null
) : BaseVH<ViewholderWishPokemonBinding, PokemonModel>(binding) {
    override fun bind(item: PokemonModel?) {
        runCatching {
            item ?: run {
                Log.e("WishPokemonViewHolder item is null")
                return
            }
        }.onSuccess { pokemon ->
            binding.apply {
                vh = this@WishPokemonVH
                dto = pokemon as? PokemonModel.Pokemon
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun goToDetail(pokemon: PokemonModel.Pokemon) {
        clickHandler?.goToDetail(1, pokemon)
    }
}