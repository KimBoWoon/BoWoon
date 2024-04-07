package com.bowoon.component.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bowoon.component.data.Pokemon
import com.bowoon.component.databinding.VhPokemonBinding
import com.bowoon.component.vh.PokemonVH

class PokemonPagingAdapter : PagingDataAdapter<Pokemon, PokemonVH>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean = oldItem.name == newItem.name && oldItem.url == newItem.url
        }
    }

    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH =
        PokemonVH(VhPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}

class PokemonPagingAdapterTemp : ListAdapter<Pokemon, PokemonVH>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Pokemon>() {
            override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean = oldItem.name == newItem.name && oldItem.url == newItem.url
        }
    }

    override fun onBindViewHolder(holder: PokemonVH, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonVH =
        PokemonVH(VhPokemonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
}