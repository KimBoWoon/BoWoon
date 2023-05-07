package com.rss_reader.vh

import androidx.recyclerview.widget.RecyclerView
import com.rss_reader.databinding.VhRssBinding
import com.rss_reader.dto.Article

class ArticleVH(
    private val binding: VhRssBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Article?) {
        item?.let {
            binding.apply {
                rss = it
            }
        }
    }
}