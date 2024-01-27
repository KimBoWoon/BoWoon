package com.bowoon.rss_reader.vh

import androidx.recyclerview.widget.RecyclerView
import com.bowoon.rss_reader.adapter.RssArticleAdapter
import com.bowoon.rss_reader.data.Rss
import com.bowoon.rss_reader.databinding.VhRssContentBinding

class RssContentVH(
    private val binding: VhRssContentBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(items: Rss) {
        binding.apply {
            rvRssList.apply {
                adapter = RssArticleAdapter {
                    scrollToPosition(it)
//                    setCurrentItem(it, true)
                }.apply {
                    submitList(items.channel?.items)
                }
            }
        }
    }
}