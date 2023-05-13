package com.rss_reader.vh

import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.rss_reader.databinding.VhRssBinding
import com.rss_reader.dto.Article
import util.ScreenUtils.dp

class ArticleVH(
    private val binding: VhRssBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Article?, last: Int) {
        item?.let {
            binding.apply {
                rss = it
                tvFeedName.isVisible = it.isHeader

                (mcvArticleRoot.layoutParams as? MarginLayoutParams)?.apply {
                    leftMargin = 10.dp
                    rightMargin = 10.dp

                    when (absoluteAdapterPosition) {
                        0 -> {
                            topMargin = 10.dp
                            bottomMargin = 5.dp
                        }
                        last - 1 -> {
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
}