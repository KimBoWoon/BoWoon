package com.lol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.base.util.Log
import com.domain.lol.dto.GameItemInfo
import com.lol.R
import com.lol.databinding.VhEmptyBinding
import com.lol.databinding.VhGameItemBinding
import com.lol.ui.vh.EmptyVH
import com.lol.ui.vh.GameItemVH

class GameItemAdapter(
    private val items: List<GameItemInfo>? = null,
    private val version: String? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val NONE = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.vh_game_item -> GameItemVH(VhGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), version)
            else -> {
                Log.e("viewholder not found")
                EmptyVH(VhEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items?.get(position)?.let {
            when (holder) {
                is GameItemVH -> holder.bind(it)
                is EmptyVH -> holder.bind(it)
            }
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        items?.let { gameItemInfo ->
            return when (gameItemInfo[position]) {
                is GameItemInfo -> R.layout.vh_game_item
                else -> NONE
            }
        } ?: run {
            return NONE
        }
    }
}