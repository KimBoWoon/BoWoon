package com.bowoon.rss_reader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bowoon.rss_reader.data.Item
import com.bowoon.rss_reader.data.Rss
import com.bowoon.rss_reader.databinding.VhArticleBinding
import com.bowoon.rss_reader.databinding.VhRssContentBinding
import com.bowoon.rss_reader.vh.ArticleVH
import com.bowoon.rss_reader.vh.RssContentVH

class RssAdapter : ListAdapter<Rss, RssContentVH>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Rss>() {
            override fun areItemsTheSame(oldItem: Rss, newItem: Rss): Boolean = false
            override fun areContentsTheSame(oldItem: Rss, newItem: Rss): Boolean = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssContentVH =
        RssContentVH(VhRssContentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RssContentVH, position: Int) {
        currentList[position]?.let { holder.bind(it) }
    }
}