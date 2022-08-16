package com.practice.paging.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domain.practice.dto.PokemonModel
import com.practice.R
import com.practice.databinding.ViewholderPokemonBinding
import com.practice.databinding.ViewholderPokemonFooterBinding
import com.practice.databinding.ViewholderPokemonHeaderBinding
import com.practice.paging.vh.PokemonFooterVH
import com.practice.paging.vh.PokemonHeaderVH
import com.practice.ui.fragments.PokemonListFragment
import com.practice.ui.vh.PokemonVH

class PokemonPagingAdapter(
    private val clickHandler: PokemonListFragment.ClickHandler
) : PagingDataAdapter<PokemonModel, RecyclerView.ViewHolder>(PokemonComparator) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            when (it) {
                is PokemonModel.Pokemon -> { (holder as? PokemonVH)?.bind(it) }
                is PokemonModel.PokemonHeader -> { (holder as? PokemonHeaderVH)?.bind(it.title ?: "PokemonHeader") }
                is PokemonModel.PokemonFooter -> { (holder as? PokemonFooterVH)?.bind(it.title ?: "PokemonFooter") }
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
                is PokemonModel.Pokemon -> R.layout.viewholder_pokemon
                is PokemonModel.PokemonHeader -> R.layout.viewholder_pokemon_header
                is PokemonModel.PokemonFooter -> R.layout.viewholder_pokemon_footer
                null -> throw UnsupportedOperationException("Unknown view")
            }
        }

        return R.layout.viewholder_load
    }

    object PokemonComparator : DiffUtil.ItemCallback<PokemonModel>() {
        override fun areItemsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
            return (oldItem as? PokemonModel.Pokemon)?.name == (newItem as? PokemonModel.Pokemon)?.name
        }
    }
}