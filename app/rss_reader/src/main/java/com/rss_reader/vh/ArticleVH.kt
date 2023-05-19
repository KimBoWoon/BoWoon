package com.rss_reader.vh

import android.content.Intent
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.rss_reader.activities.ArticleDetailActivity
import com.rss_reader.databinding.VhRssBinding
import com.rss_reader.dto.Article
import util.ScreenUtils.dp

class ArticleVH(
    private val binding: VhRssBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Article?, last: Int) {
        item?.let {
            binding.apply {
                vh = this@ArticleVH
                rss = it

                (mcvArticleRoot.layoutParams as? MarginLayoutParams)?.apply {
                    leftMargin = 10.dp
                    rightMargin = 10.dp

                    when (absoluteAdapterPosition) {
                        0 -> {
                            topMargin = 10.dp
                            bottomMargin = 5.dp
                        }
                        last -> {
                            topMargin = 5.dp
                            bottomMargin = 10.dp
                        }
                        else -> {
                            topMargin = 5.dp
                            bottomMargin = 5.dp
                        }
                    }
                }

                executePendingBindings()
            }
        }
    }

    fun articleClick(article: Article?) {
        article?.let {
            binding.root.context.startActivity(
                Intent(
                    binding.root.context,
                    ArticleDetailActivity::class.java
                ).apply {
                    putExtra("loadUrl", it.linkUrl)
                })
        }
    }
}