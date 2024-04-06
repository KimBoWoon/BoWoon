package com.bowoon.component.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.component.R
import com.bowoon.component.data.Pokemon
import com.bowoon.component.databinding.VhPokemonBinding
import com.bowoon.component.vh.PokemonVH

class PokemonPagingAdapter(
) : PagingDataAdapter<Pokemon, RecyclerView.ViewHolder>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean =
                oldItem.name == newItem.name
        }
    }

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
            R.layout.vh_pokemon -> PokemonVH(VhPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw UnsupportedOperationException("Unknown view")
        }

    override fun getItemViewType(position: Int): Int =
        getItem(position)?.let {
            when (it) {
                is Pokemon -> R.layout.vh_pokemon
                else -> -1
            }
        } ?: -1
}