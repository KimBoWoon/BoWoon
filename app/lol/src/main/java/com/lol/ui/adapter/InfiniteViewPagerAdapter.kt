package com.lol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.lol.R
import com.bowoon.lol.databinding.VhChampionSkinBinding
import com.bowoon.lol.databinding.VhEmptyBinding
import com.domain.lol.dto.Skin
import com.lol.ui.vh.ChampionSkinVH
import com.lol.ui.vh.EmptyVH
import com.data.util.Log

class InfiniteViewPagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val NONE = -1

    var items: List<Any?>? = mutableListOf()
        set(value) {
            value?.let {
                field = if (value.size > 1) {
                    mutableListOf<Any?>().apply {
                        add(value.last())
                        addAll(value)
                        add(value.first())
                    }
                } else {
                    value
                }
            }
            notifyDataSetChanged()
        }
        get() = field?.toList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.vh_champion_skin -> ChampionSkinVH(VhChampionSkinBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> {
                Log.e("viewholder not found")
                EmptyVH(VhEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items?.get(position)?.let {
            when (holder) {
                is ChampionSkinVH -> holder.bind(it as? Skin)
            }
        }
    }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        items?.get(position)?.let {
            return when (it) {
                is Skin -> R.layout.vh_champion_skin
                else -> {
                    Log.e("view type not found >>>>> $it")
                    NONE
                }
            }
        }

        Log.e("infiniteViewPager", "data is null")
        return NONE
    }
}