package com.bowoon.rss_reader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bowoon.rss_reader.data.Item
import com.bowoon.rss_reader.databinding.VhArticleBinding
import com.bowoon.rss_reader.vh.ArticleVH

class RssArticleAdapter : ListAdapter<Item, ArticleVH>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem.link == newItem.link && oldItem.pubDate == newItem.pubDate
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH =
        ArticleVH(VhArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        currentList[position]?.let { holder.bind(it) }
    }
}