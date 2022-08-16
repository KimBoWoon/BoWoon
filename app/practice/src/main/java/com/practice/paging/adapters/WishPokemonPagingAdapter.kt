package com.practice.paging.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.domain.practice.dto.PokemonModel
import com.practice.R
import com.practice.databinding.ViewholderWishPokemonBinding
import com.practice.ui.fragments.WishPokemonList
import com.practice.ui.vh.WishPokemonVH

class WishPokemonPagingAdapter(
    private val clickHandler: WishPokemonList.ClickHandler? = null
) : PagingDataAdapter<PokemonModel, WishPokemonVH>(WishPokemonComparator) {
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

    object WishPokemonComparator : DiffUtil.ItemCallback<PokemonModel>() {
        override fun areItemsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
            return (oldItem as? PokemonModel.Pokemon)?.name == (newItem as? PokemonModel.Pokemon)?.name
        }
    }
}