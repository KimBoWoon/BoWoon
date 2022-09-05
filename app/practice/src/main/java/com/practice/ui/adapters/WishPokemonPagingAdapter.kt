package com.practice.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.domain.practice.dto.Pokemon
import com.practice.R
import com.practice.databinding.ViewholderWishPokemonBinding
import com.practice.ui.fragments.WishPokemonListFragment
import com.practice.ui.vh.WishPokemonVH

class WishPokemonPagingAdapter(
    private val clickHandler: WishPokemonListFragment.ClickHandler? = null
) : PagingDataAdapter<Pokemon, WishPokemonVH>(WishPokemonComparator) {
    override fun onBindViewHolder(holder: WishPokemonVH, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishPokemonVH =
        WishPokemonVH(ViewholderWishPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickHandler)

    override fun getItemViewType(position: Int): Int =
        when (position < itemCount) {
            true -> {
                getItem(position)?.let {
                    when (it) {
                        is Pokemon -> R.layout.viewholder_pokemon
                        else -> -1
                    }
                } ?: -1
            }
            false -> -1
        }

    object WishPokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
            oldItem.name == newItem.name
    }
}