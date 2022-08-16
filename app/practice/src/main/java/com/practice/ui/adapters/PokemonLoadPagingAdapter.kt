package com.practice.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.practice.databinding.ViewholderLoadBinding
import com.practice.ui.vh.LoadStateVH

class PokemonLoadPagingAdapter(
    private val retry: (() -> Unit)? = null
) : LoadStateAdapter<LoadStateVH>() {
    override fun onBindViewHolder(holder: LoadStateVH, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateVH =
        LoadStateVH(ViewholderLoadBinding.inflate(LayoutInflater.from(parent.context), parent, false), retry)
}