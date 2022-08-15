package com.practice.ui.vh

import com.data.practice.room.WishPokemon
import com.practice.base.BaseVH
import com.practice.databinding.ViewholderWishPokemonBinding
import util.Log

class WishPokemonVH(
    override val binding: ViewholderWishPokemonBinding,
) : BaseVH<ViewholderWishPokemonBinding, WishPokemon>(binding) {
    override fun bind(item: WishPokemon?) {
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

    fun goToDetail(pokemon: WishPokemon) {
//        fragmentVM?.goToDetail?.value = Pair(WISH_POKEMON_DETAIL, pokemon)
    }
}