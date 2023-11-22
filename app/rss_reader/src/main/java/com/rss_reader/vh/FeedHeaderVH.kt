package com.rss_reader.vh

import androidx.recyclerview.widget.RecyclerView
import com.bowoon.rss_reader.databinding.VhFeedHeaderBinding
import com.rss_reader.dto.Article

class FeedHeaderVH(
    private val binding: VhFeedHeaderBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Article?) {
        item?.let {
            binding.apply {
                rss = it

                executePendingBindings()
            }
        }
    }
}