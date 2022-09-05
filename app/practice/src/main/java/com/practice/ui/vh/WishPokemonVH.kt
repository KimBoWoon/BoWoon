package com.practice.ui.vh

import com.domain.practice.dto.Pokemon
import com.practice.base.BaseVH
import com.practice.databinding.ViewholderWishPokemonBinding
import com.practice.ui.fragments.WishPokemonListFragment
import util.Log

class WishPokemonVH(
    override val binding: ViewholderWishPokemonBinding,
    private val clickHandler: WishPokemonListFragment.ClickHandler? = null
) : BaseVH<ViewholderWishPokemonBinding, Pokemon>(binding) {
    override fun bind(item: Pokemon?) {
        runCatching {
            item ?: run {
                Log.e("WishPokemonViewHolder item is null")
                return
            }
        }.onSuccess { pokemon ->
            binding.apply {
                vh = this@WishPokemonVH
                dto = pokemon
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun goToDetail(pokemon: Pokemon) {
        clickHandler?.goToDetail(1, pokemon)
    }
}