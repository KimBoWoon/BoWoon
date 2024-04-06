package com.bowoon.component.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.component.R
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.VhImageComponentBinding
import com.bowoon.component.databinding.VhListComponentBinding
import com.bowoon.component.databinding.VhTabComponentBinding
import com.bowoon.component.databinding.VhTextComponentBinding
import com.bowoon.component.ui.MainVM
import com.bowoon.component.vh.ImageComponentVH
import com.bowoon.component.vh.ListComponentVH
import com.bowoon.component.vh.TabComponentVH
import com.bowoon.component.vh.TextComponentVH

class ComponentAdapter(
    private val vm: MainVM
) : ListAdapter<Components, RecyclerView.ViewHolder>(diff) {
    companion object {
        private const val NO_ID = -1
        private val diff = object : DiffUtil.ItemCallback<Components>() {
            override fun areItemsTheSame(oldItem: Components, newItem: Components): Boolean = when {
                oldItem is Components.TextComponent && newItem is Components.TextComponent -> oldItem.text == newItem.text
                oldItem is Components.ImageComponent && newItem is Components.ImageComponent -> oldItem.url == newItem.url
                oldItem is Components.TabComponent && newItem is Components.TabComponent -> oldItem.mode == newItem.mode
                oldItem is Components.ListComponent && newItem is Components.ListComponent -> oldItem.url == newItem.url
                else -> false
            }

            override fun areContentsTheSame(oldItem: Components, newItem: Components): Boolean = when {
                oldItem is Components.TextComponent && newItem is Components.TextComponent -> oldItem.color == newItem.color && oldItem.size == newItem.size && oldItem.style?.containsAll(newItem.style ?: emptyList()) ?: false
                oldItem is Components.ImageComponent && newItem is Components.ImageComponent -> oldItem.radius == newItem.radius && oldItem.topLeftRadius == newItem.topLeftRadius && oldItem.topRightRadius == newItem.topRightRadius && oldItem.bottomLeftRadius == newItem.bottomLeftRadius && oldItem.bottomRightRadius == newItem.bottomRightRadius
                oldItem is Components.TabComponent && newItem is Components.TabComponent -> oldItem.tabs?.containsAll(newItem.tabs ?: emptyList()) ?: false
                oldItem is Components.ListComponent && newItem is Components.ListComponent -> oldItem.url == newItem.url && oldItem.listData?.containsAll(newItem.listData ?: emptyList()) ?: false
                else -> false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.vh_text_component -> TextComponentVH(VhTextComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        R.layout.vh_image_component -> ImageComponentVH(VhImageComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        R.layout.vh_tab_component -> TabComponentVH(VhTabComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        R.layout.vh_list_component -> ListComponentVH(VhListComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false), vm)
        else -> throw RuntimeException("viewholder not found...")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let {
            when (holder) {
                is TextComponentVH -> holder.bind(it as? Components.TextComponent)
                is ImageComponentVH -> holder.bind(it as? Components.ImageComponent)
                is TabComponentVH -> holder.bind(it as? Components.TabComponent)
                is ListComponentVH -> holder.bind(it as? Components.ListComponent)
            }
        }
    }

    override fun getItemViewType(position: Int): Int = currentList[position]?.let {
        when (it) {
            is Components.TextComponent -> R.layout.vh_text_component
            is Components.ImageComponent -> R.layout.vh_image_component
            is Components.TabComponent -> R.layout.vh_tab_component
            is Components.ListComponent -> R.layout.vh_list_component
        }
    } ?: NO_ID
}