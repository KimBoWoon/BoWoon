package com.practice.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domain.practice.dto.SealedPokemon
import com.practice.R
import com.practice.databinding.ViewholderPokemonBinding
import com.practice.databinding.ViewholderPokemonFooterBinding
import com.practice.databinding.ViewholderPokemonHeaderBinding
import com.practice.ui.vh.PokemonFooterVH
import com.practice.ui.vh.PokemonHeaderVH
import com.practice.ui.fragments.PokemonListFragment
import com.practice.ui.vh.PokemonVH

class PokemonPagingAdapter(
    private val clickHandler: PokemonListFragment.ClickHandler
) : PagingDataAdapter<SealedPokemon, RecyclerView.ViewHolder>(PokemonComparator) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is SealedPokemon.Pokemon -> { (holder as? PokemonVH)?.bind(it) }
                is SealedPokemon.PokemonHeader -> { (holder as? PokemonHeaderVH)?.bind(it.title ?: "PokemonHeader") }
                is SealedPokemon.PokemonFooter -> { (holder as? PokemonFooterVH)?.bind(it.title ?: "PokemonFooter") }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.viewholder_pokemon -> { PokemonVH(ViewholderPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickHandler) }
            R.layout.viewholder_pokemon_header -> { PokemonHeaderVH(ViewholderPokemonHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
            R.layout.viewholder_pokemon_footer -> { PokemonFooterVH(ViewholderPokemonFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)) }
            else -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < itemCount) {
            return when (getItem(position)) {
                is SealedPokemon.Pokemon -> R.layout.viewholder_pokemon
                is SealedPokemon.PokemonHeader -> R.layout.viewholder_pokemon_header
                is SealedPokemon.PokemonFooter -> R.layout.viewholder_pokemon_footer
                null -> throw UnsupportedOperationException("Unknown view")
            }
        }

        return R.layout.viewholder_load
    }

    object PokemonComparator : DiffUtil.ItemCallback<SealedPokemon>() {
        override fun areItemsTheSame(oldItem: SealedPokemon, newItem: SealedPokemon): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SealedPokemon, newItem: SealedPokemon): Boolean {
            return (oldItem as? SealedPokemon.Pokemon)?.name == (newItem as? SealedPokemon.Pokemon)?.name
        }
    }
}