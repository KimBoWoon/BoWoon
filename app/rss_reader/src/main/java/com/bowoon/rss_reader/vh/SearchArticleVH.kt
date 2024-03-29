package com.bowoon.rss_reader.vh

import android.content.Intent
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.rss_reader.activities.ArticleDetailActivity
import com.bowoon.rss_reader.data.Article
import com.bowoon.rss_reader.databinding.VhRssBinding

class SearchArticleVH(
    private val binding: VhRssBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Article?, last: Int) {
        item?.let {
            binding.apply {
                vh = this@SearchArticleVH
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