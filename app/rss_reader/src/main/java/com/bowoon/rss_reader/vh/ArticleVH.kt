package com.bowoon.rss_reader.vh

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.Log
import com.bowoon.imageloader.ImageLoader
import com.bowoon.imageloader.ImageOptions
import com.bowoon.rss_reader.activities.ArticleDetailActivity
import com.bowoon.rss_reader.data.Item
import com.bowoon.rss_reader.databinding.VhArticleBinding
import com.bowoon.timer.Timer
import com.bowoon.timer.TimerStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup

class ArticleVH(
    private val binding: VhArticleBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val TAG = "rss_reader_article_vh"
    }

    private var timer: Timer? = Timer()

    fun bind(items: Item) {
        binding.apply {
            vh = this@ArticleVH
            item = items

//            CoroutineScope(Dispatchers.IO).launch {
//                timer?.timerFlow?.collectLatest { status ->
//                    when (status) {
//                        TimerStatus.Finish -> Log.d(TAG, "TimerStatus.Finish")
//                        TimerStatus.Pause -> Log.d(TAG, "TimerStatus.Pause")
//                        TimerStatus.Ready -> {
//                            Log.d(TAG, "TimerStatus.Ready")
//                            timer?.let {
//                                it.start {
//                                    binding.pbLoading.progress += 1
//                                    if (binding.pbLoading.progress == RssContentVH.DELAY_TIME_SECOND.toInt()) {
//                                        it.stop()
//                                        timer = null
//                                        binding.pbLoading.progress = 0
//                                    }
//                                }
//                            }
//                        }
//                        TimerStatus.Start -> Log.d(TAG, "TimerStatus.Start")
//                        TimerStatus.Stop -> Log.d(TAG, "TimerStatus.Stop")
//                        TimerStatus.UnInitialized -> {
//                            Log.d(TAG, "TimerStatus.UnInitialized")
//                            timer?.ready(1)
//                        }
//                    }
//                }
//            }

            pbLoading.apply {
                max = RssContentVH.DELAY_TIME_SECOND.toInt()
            }

            CoroutineScope(Dispatchers.IO).launch {
                items.link?.let { articleLink ->
                    if (articleLink.isNotEmpty() && articleLink.isNotBlank()) {
                        runCatching {
                            Jsoup.connect(articleLink).get()
                                .select("meta[property=og:image]")
                                .attr("content")
                        }.onSuccess { imgUrl ->
                            withContext(Dispatchers.Main) {
                                ImageLoader.load(
                                    binding.root.context,
                                    ivArticleImage,
                                    imgUrl,
                                    ImageOptions(placeholderDrawable = ColorDrawable(Color.BLACK))
                                )
                            }
                        }.onFailure { e ->
                            Log.printStackTrace(e)
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
}