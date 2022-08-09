package com.lol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.domain.lol.dto.ChampionInfo
import com.domain.lol.dto.GameItemInfo
import com.domain.lol.dto.Skin
import com.domain.lol.dto.Spell
import com.lol.R
import com.lol.databinding.*
import com.lol.ui.fragments.ChampionListFragment
import com.lol.ui.vh.*
import util.Log

class LolAdapter(
    private val items: List<Any?>? = null,
    private val handler: ChampionListFragment.ClickHandler? = null,
    private val version: String? = null,
    private val championName: String? = null
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val NONE = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.vh_champion -> ChampionVH(VhChampionBinding.inflate(LayoutInflater.from(parent.context), parent, false), handler)
            R.layout.vh_game_item -> GameItemVH(VhGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), version)
            R.layout.vh_champion_skin -> ChampionSkinVH(VhChampionSkinBinding.inflate(LayoutInflater.from(parent.context), parent, false), championName)
            R.layout.vh_champion_spell -> ChampionSpellVH(VhChampionSpellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> {
                Log.e("viewholder not found")
                EmptyVH(VhEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items?.get(position)?.let {
            when (holder) {
                is ChampionVH -> holder.bind(it as? ChampionInfo)
                is GameItemVH -> holder.bind(it as? GameItemInfo)
                is ChampionSkinVH -> holder.bind(it as? Skin)
                is ChampionSpellVH -> holder.bind(it as? Spell)
                is EmptyVH -> holder.bind(it)
            }
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        items?.let { championInfoList ->
            return when (championInfoList[position]) {
                is ChampionInfo -> R.layout.vh_champion
                is GameItemInfo -> R.layout.vh_game_item
                is Skin -> R.layout.vh_champion_skin
                is Spell -> R.layout.vh_champion_spell
                else -> NONE
            }
        } ?: run {
            return NONE
        }
    }
}