package com.bowoon.lol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.Log
import com.bowoon.lol.R
import com.bowoon.lol.base.BaseFragment
import com.bowoon.lol.data.Champion
import com.bowoon.lol.data.GameItemInfo
import com.bowoon.lol.data.Passive
import com.bowoon.lol.data.Skin
import com.bowoon.lol.data.Spell
import com.bowoon.lol.databinding.VhChampionBinding
import com.bowoon.lol.databinding.VhChampionPassiveBinding
import com.bowoon.lol.databinding.VhChampionSkinBinding
import com.bowoon.lol.databinding.VhChampionSpellBinding
import com.bowoon.lol.databinding.VhEmptyBinding
import com.bowoon.lol.databinding.VhGameItemBinding
import com.bowoon.lol.ui.vh.ChampionPassiveVH
import com.bowoon.lol.ui.vh.ChampionSkinVH
import com.bowoon.lol.ui.vh.ChampionSpellVH
import com.bowoon.lol.ui.vh.ChampionVH
import com.bowoon.lol.ui.vh.EmptyVH
import com.bowoon.lol.ui.vh.GameItemVH

class LolAdapter(
    private val items: List<Any?>? = null,
    private val handler: BaseFragment<out ViewDataBinding>.ClickHandler? = null,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val NONE = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.vh_champion -> ChampionVH(VhChampionBinding.inflate(LayoutInflater.from(parent.context), parent, false), handler)
            R.layout.vh_game_item -> GameItemVH(VhGameItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), handler)
            R.layout.vh_champion_skin -> ChampionSkinVH(VhChampionSkinBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.vh_champion_spell -> ChampionSpellVH(VhChampionSpellBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            R.layout.vh_champion_passive -> ChampionPassiveVH(VhChampionPassiveBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> {
                Log.e("viewholder not found")
                EmptyVH(VhEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items?.get(position)?.let {
            when (holder) {
                is ChampionVH -> holder.bind(it as? Champion)
                is GameItemVH -> holder.bind(it as? GameItemInfo)
                is ChampionSkinVH -> holder.bind(it as? Skin)
                is ChampionSpellVH -> holder.bind(it as? Spell)
                is ChampionPassiveVH -> holder.bind(it as? Passive)
                is EmptyVH -> holder.bind(it)
            }
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        items?.let { championInfoList ->
            return when (championInfoList[position]) {
                is Champion -> R.layout.vh_champion
                is GameItemInfo -> R.layout.vh_game_item
                is Skin -> R.layout.vh_champion_skin
                is Spell -> R.layout.vh_champion_spell
                is Passive -> R.layout.vh_champion_passive
                else -> NONE
            }
        } ?: run {
            return NONE
        }
    }
}