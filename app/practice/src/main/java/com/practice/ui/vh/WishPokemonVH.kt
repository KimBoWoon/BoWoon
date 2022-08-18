package com.practice.ui.vh

import com.domain.practice.dto.SealedPokemon
import com.practice.base.BaseVH
import com.practice.databinding.ViewholderWishPokemonBinding
import com.practice.ui.fragments.WishPokemonListFragment
import util.Log

class WishPokemonVH(
    override val binding: ViewholderWishPokemonBinding,
    private val clickHandler: WishPokemonListFragment.ClickHandler? = null
) : BaseVH<ViewholderWishPokemonBinding, SealedPokemon>(binding) {
    override fun bind(item: SealedPokemon?) {
        runCatching {
            item ?: run {
                Log.e("WishPokemonViewHolder item is null")
                return
            }
        }.onSuccess { pokemon ->
            binding.apply {
                vh = this@WishPokemonVH
                dto = pokemon as? SealedPokemon.Pokemon
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun goToDetail(pokemon: SealedPokemon.Pokemon) {
        clickHandler?.goToDetail(1, pokemon)
    }
}