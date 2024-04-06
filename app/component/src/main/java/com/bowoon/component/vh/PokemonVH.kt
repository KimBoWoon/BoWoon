package com.bowoon.component.vh

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.component.R
import com.bowoon.component.data.Pokemon
import com.bowoon.component.databinding.VhPokemonBinding
import com.bowoon.imageloader.ImageLoader
import com.bowoon.imageloader.ImageOptions

class PokemonVH(
    val binding: VhPokemonBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Pokemon?) {
        item?.let { pokemon ->
            binding.apply {
                vh = this@PokemonVH
                dto = pokemon
                ImageLoader.load(
                    binding.root.context,
                    ivPokemonImage,
                    pokemon.getImageUrl(),
                    ImageOptions(
                        placeholderDrawable = ColorDrawable(Color.TRANSPARENT),
                        error = R.drawable.ic_launcher_foreground,
                        radius = 50
                    )
                )
            }
        }
    }
}