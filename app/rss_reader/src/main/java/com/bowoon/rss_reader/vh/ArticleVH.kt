package com.bowoon.rss_reader.vh

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.imageloader.ImageLoader
import com.bowoon.imageloader.ImageOptions
import com.bowoon.rss_reader.activities.ArticleDetailActivity
import com.bowoon.rss_reader.data.Item
import com.bowoon.rss_reader.databinding.VhArticleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class ArticleVH(
    private val binding: VhArticleBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(items: Item) {
        binding.apply {
            vh = this@ArticleVH
            item = items
            CoroutineScope(Dispatchers.IO).launch {
                Jsoup.connect(items.link ?: "").get().select("meta[property=og:image]").attr("content").also {
                    withContext(Dispatchers.Main) {
                        ImageLoader.load(binding.root.context, ivArticleImage, it, ImageOptions(placeholderDrawable = ColorDrawable(
                            Color.YELLOW)
                        )
                        )
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