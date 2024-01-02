package com.bowoon.rss_reader.vh

import androidx.recyclerview.widget.RecyclerView
import com.bowoon.rss_reader.data.Article
import com.bowoon.rss_reader.databinding.VhFeedHeaderBinding

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