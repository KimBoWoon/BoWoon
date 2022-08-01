package com.lol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.data.lol.util.Log
import com.domain.lol.dto.ChampionInfo
import com.lol.R
import com.lol.databinding.VhChampionBinding
import com.lol.databinding.VhEmptyBinding
import com.lol.ui.vh.ChampionVH
import com.lol.ui.vh.EmptyVH

class ChampionAdapter(
    private val items: List<ChampionInfo?>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val NONE = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.vh_champion -> ChampionVH(VhChampionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> {
                Log.e("viewholder not found")
                EmptyVH(VhEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChampionVH -> {
                holder.bind(items?.get(position))
            }
            is EmptyVH -> {
                holder.bind(items?.get(position))
            }
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        items?.let { championInfoList ->
            return when (championInfoList[position]) {
                is ChampionInfo -> R.layout.vh_champion
                else -> NONE
            }
        } ?: run {
            return NONE
        }
    }
}