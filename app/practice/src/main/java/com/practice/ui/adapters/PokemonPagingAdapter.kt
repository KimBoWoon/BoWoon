package com.practice.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.practice.R
import com.bowoon.practice.databinding.ViewholderPokemonBinding
import com.domain.practice.dto.Pokemon
import com.practice.ui.fragments.PokemonListFragment
import com.practice.ui.vh.PokemonVH

class PokemonPagingAdapter(
    private val clickHandler: PokemonListFragment.ClickHandler
) : PagingDataAdapter<Pokemon, RecyclerView.ViewHolder>(PokemonComparator) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (holder) {
                is PokemonVH -> holder.bind(it)
                else -> throw UnsupportedOperationException("viewholder not found!")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.viewholder_pokemon -> PokemonVH(ViewholderPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickHandler)
            else -> throw UnsupportedOperationException("Unknown view")
        }

    override fun getItemViewType(position: Int): Int =
        when (position < itemCount) {
            true -> {
                getItem(position)?.let {
                    when (it) {
                        is Pokemon -> R.layout.viewholder_pokemon
                        else -> throw UnsupportedOperationException("Unknown view")
                    }
                } ?: -1
            }
            false -> -1
        }

    object PokemonComparator : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
            oldItem.name == newItem.name
    }
}