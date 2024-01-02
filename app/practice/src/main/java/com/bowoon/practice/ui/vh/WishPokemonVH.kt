package com.bowoon.practice.ui.vh

import com.bowoon.commonutils.Log
import com.bowoon.imageloader.ImageLoader
import com.bowoon.practice.base.BaseVH
import com.bowoon.practice.data.Pokemon
import com.bowoon.practice.databinding.ViewholderWishPokemonBinding
import com.bowoon.practice.ui.fragments.WishPokemonListFragment

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
                ImageLoader.load(binding.root.context, ivPokemonImage, pokemon.getImageUrl())
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun goToDetail(pokemon: Pokemon) {
        clickHandler?.goToDetail(1, pokemon)
    }
}