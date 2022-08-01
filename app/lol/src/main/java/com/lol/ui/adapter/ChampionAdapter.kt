package com.lol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.domain.lol.dto.ChampionInfo
import com.lol.R
import com.lol.databinding.VhChampionBinding
import com.lol.ui.vh.ChampionVH

class ChampionAdapter(
    private val items: List<ChampionInfo?>? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val NONE = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.vh_champion -> ChampionVH(VhChampionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw RuntimeException("viewholder not found")
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChampionVH -> {
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