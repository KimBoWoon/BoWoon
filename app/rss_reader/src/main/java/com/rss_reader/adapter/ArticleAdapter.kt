package com.rss_reader.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.rss_reader.databinding.VhRssBinding
import com.rss_reader.dto.Article
import com.rss_reader.vh.ArticleVH
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleAdapter(
    private val loader: ArticleLoader? = null
) : ListAdapter<Article, ArticleVH>(diff) {
    companion object {
        val diff = object : DiffUtil.ItemCallback<Article>() {
            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.feed == newItem.feed

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean =
                oldItem.title == newItem.title && oldItem.summary == newItem.summary
        }
    }

    private var loading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH =
        ArticleVH(VhRssBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        currentList[position]?.let {
            holder.bind(it, currentList.lastIndex)
        }

        if (loader != null && !loading && position >= currentList.size - 2) {
            loading = true

            CoroutineScope(Dispatchers.IO).launch {
                loader.loadMore()
                loading = false
            }
        }
    }
}

interface ArticleLoader {
    suspend fun loadMore()
}