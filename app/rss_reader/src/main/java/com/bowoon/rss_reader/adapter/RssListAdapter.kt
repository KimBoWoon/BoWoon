package com.bowoon.rss_reader.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.imageloader.ImageLoader
import com.bowoon.rss_reader.activities.ArticleDetailActivity
import com.bowoon.rss_reader.data.Item
import com.bowoon.rss_reader.data.Rss
import com.bowoon.rss_reader.databinding.VhArticleBinding
import com.bowoon.rss_reader.databinding.VhRssContentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class RssListAdapter : ListAdapter<Rss, RssListAdapter.RssContentVH>(diff) {
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

    inner class RssContentVH(
        private val binding: VhRssContentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Rss) {
            binding.apply {
                rvRssList.apply {
                    adapter = ArticleAdapter2().apply {
                        submitList(items.channel?.items)
                    }
                }
            }
        }
    }
}

class ArticleAdapter2 : ListAdapter<Item, ArticleAdapter2.ArticleVH>(diff) {
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

    inner class ArticleVH(
        private val binding: VhArticleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(items: Item) {
            binding.apply {
                vh = this@ArticleVH
                item = items
                CoroutineScope(Dispatchers.IO).launch {
                    Jsoup.connect(items.link ?: "").get().select("meta[property=og:image]").attr("content").also {
                        withContext(Dispatchers.Main) {
                            ImageLoader.load(binding.root.context, ivArticleImage, it)
                        }
                    }
                }
            }
        }

        fun onClick(item: Item?) {
            item?.let {
                binding.root.context.startActivity(
                    Intent(
                        binding.root.context,
                        ArticleDetailActivity::class.java
                    ).apply {
                        putExtra("loadUrl", it.link)
                    })
            }
        }
    }
}