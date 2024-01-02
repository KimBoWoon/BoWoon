package com.bowoon.practice.ui.vh

import androidx.core.content.res.ResourcesCompat
import com.bowoon.commonutils.Log
import com.bowoon.imageloader.ImageLoader
import com.bowoon.imageloader.ImageOptions
import com.bowoon.practice.R
import com.bowoon.practice.base.BaseVH
import com.bowoon.practice.data.Pokemon
import com.bowoon.practice.databinding.ViewholderPokemonBinding
import com.bowoon.practice.ui.fragments.PokemonListFragment

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
                ImageLoader.load(
                    binding.root.context,
                    ivPokemonImage,
                    pokemon.getImageUrl(),
                    ImageOptions(
                        placeholderDrawable = ResourcesCompat.getDrawable(binding.root.context.resources, R.drawable.ic_launcher_background, null),
                        error = R.drawable.ic_launcher_foreground,
                        radius = 50
                    )
                )
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun goToDetail(pokemon: Pokemon) {
        clickHandler.goToDetail(pokemon, 0)
    }
}