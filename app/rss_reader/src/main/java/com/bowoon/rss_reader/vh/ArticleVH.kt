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
import com.bowoon.timer.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class ArticleVH(
    private val binding: VhArticleBinding,
    private val nextArticleLambda: ((Int) -> Unit)? = null
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val TIME = 200
    }

    private var timer: Timer? = Timer()

    fun bind(items: Item) {
        binding.apply {
            vh = this@ArticleVH
            item = items

            pbLoading.apply {
                max = TIME
            }

            CoroutineScope(Dispatchers.IO).launch {
                items.link?.let { articleLink ->
                    if (articleLink.isNotEmpty() && articleLink.isNotBlank()) {
                        Jsoup.connect(articleLink).get()
                            .select("meta[property=og:image]")
                            .attr("content")
                            .also {
                                withContext(Dispatchers.Main) {
                                    ImageLoader.load(
                                        binding.root.context,
                                        ivArticleImage,
                                        it,
                                        ImageOptions(placeholderDrawable = ColorDrawable(Color.BLACK))
                                    )
                                }
                            }
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

    fun startTimer(lastIndex: Int) {
        timer?.startPeriodTimer(10) {
            binding.pbLoading.progress++
            if (binding.pbLoading.progress == TIME) {
                timer?.stopTimer()
                CoroutineScope(Dispatchers.Main).launch {
                    nextArticleLambda?.invoke(if (lastIndex == absoluteAdapterPosition) 0 else absoluteAdapterPosition + 1)
                }
            }
        }
    }

    fun stopTimer() {
        timer?.stopTimer()
//        timer = null
        binding.pbLoading.progress = 0
    }
}