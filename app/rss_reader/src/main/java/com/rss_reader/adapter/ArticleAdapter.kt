package com.rss_reader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rss_reader.R
import com.rss_reader.databinding.VhFeedHeaderBinding
import com.rss_reader.databinding.VhRssBinding
import com.rss_reader.dto.Article
import com.rss_reader.vh.ArticleVH
import com.rss_reader.vh.FeedHeaderVH

class ArticleAdapter : ListAdapter<Article, RecyclerView.ViewHolder>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.feed == newItem.feed

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.encodeTitle == newItem.encodeTitle && oldItem.encodeSummary == newItem.encodeSummary
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.vh_feed_header -> FeedHeaderVH(VhFeedHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        R.layout.vh_rss -> ArticleVH(VhRssBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        else -> throw RuntimeException("viewholder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currentList[position]?.let {
            when (holder) {
                is FeedHeaderVH -> holder.bind(it)
                is ArticleVH -> holder.bind(it, currentList.lastIndex)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        currentList[position]?.let {
            return when (it.isHeader) {
                true -> R.layout.vh_feed_header
                false -> R.layout.vh_rss
            }
        }
        throw RuntimeException("not supported viewtype")
    }

    fun isHeader(position: Int): Boolean = currentList[position]?.isHeader ?: false

    fun getHeaderLayout(list: RecyclerView, position: Int): View? =
        currentList[position]?.let {
            VhFeedHeaderBinding.inflate(LayoutInflater.from(list.context), list, false).apply {
                rss = it
                executePendingBindings()
            }.root
        }
}