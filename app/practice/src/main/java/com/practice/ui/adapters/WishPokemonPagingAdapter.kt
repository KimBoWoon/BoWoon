package com.practice.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.domain.practice.dto.SealedPokemon
import com.practice.R
import com.practice.databinding.ViewholderWishPokemonBinding
import com.practice.ui.fragments.WishPokemonListFragment
import com.practice.ui.vh.WishPokemonVH

class WishPokemonPagingAdapter(
    private val clickHandler: WishPokemonListFragment.ClickHandler? = null
) : PagingDataAdapter<SealedPokemon, WishPokemonVH>(WishPokemonComparator) {
    override fun onBindViewHolder(holder: WishPokemonVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishPokemonVH {
        return WishPokemonVH(ViewholderWishPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickHandler)
    }

    override fun getItemViewType(position: Int): Int =
        if (position == itemCount) {
            R.layout.viewholder_load
        } else {
            R.layout.viewholder_pokemon
        }

    object WishPokemonComparator : DiffUtil.ItemCallback<SealedPokemon>() {
        override fun areItemsTheSame(oldItem: SealedPokemon, newItem: SealedPokemon): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SealedPokemon, newItem: SealedPokemon): Boolean {
            return (oldItem as? SealedPokemon.Pokemon)?.name == (newItem as? SealedPokemon.Pokemon)?.name
        }
    }
}