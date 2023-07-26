package com.lol.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.lol.databinding.VhRelatedItemBinding
import com.domain.lol.dto.GameItemInfo
import com.lol.ui.vh.RelatedItemVH

class RelatedItemAdapter(
    private val items: List<GameItemInfo>? = null
) : RecyclerView.Adapter<RelatedItemVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedItemVH =
        RelatedItemVH(VhRelatedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RelatedItemVH, position: Int) {
        holder.bind(items?.get(position))
    }

    override fun getItemCount(): Int = items?.size ?: 0
}