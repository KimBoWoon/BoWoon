package com.bowoon.rss_reader.vh

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bowoon.commonutils.Log
import com.bowoon.rss_reader.adapter.InfiniteScrollAdapter
import com.bowoon.rss_reader.data.Rss
import com.bowoon.rss_reader.databinding.VhRssContentBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RssContentVH(
    private val binding: VhRssContentBinding
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private const val TAG = "rss_reader_rss_content_vh"
        const val DELAY_TIME_SECOND = 2000L
    }

    private var autoScrollJob: Job? = null

    fun bind(items: Rss) {
        binding.apply {
            rvRssList.apply {
//                offscreenPageLimit = items.channel?.items?.size ?: 1
                adapter = InfiniteScrollAdapter().apply {
                    submitList(items.channel?.items?.take(3))
                }
                setCurrentItem(1, false)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {}
                    override fun onPageScrollStateChanged(state: Int) {
                        val listSize = (adapter?.itemCount ?: 0)
                        when (state) {
                            ViewPager2.SCROLL_STATE_IDLE -> {
                                if (autoScrollJob?.isCancelled == true) {
                                    Log.d(TAG, "auto scroll start")
                                    startAutoScroll()
                                }
                                when (currentItem) {
                                    listSize - 1 -> setCurrentItem(1, false)
                                    0 -> setCurrentItem(listSize - 2, false)
                                }
                            }
                            ViewPager2.SCROLL_STATE_DRAGGING -> {
                                if (autoScrollJob?.isActive == true) {
                                    Log.d(TAG, "auto scroll cancel")
                                    autoScrollJob?.cancel()
                                }
                            }
                            ViewPager2.SCROLL_STATE_SETTLING -> Log.d(TAG, "ViewPager2.SCROLL_STATE_SETTLING")
                        }
                    }
                })
            }
        }
    }

    fun startAutoScroll() {
        Log.d(TAG, "start auto scroll")
        autoScrollJob = flow {
            while (true) {
                delay(DELAY_TIME_SECOND)
                emit(Unit)
            }
        }.onEach {
            binding.rvRssList.isUserInputEnabled = false
            binding.rvRssList.currentItem += 1
            binding.rvRssList.isUserInputEnabled = true
        }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    fun stopAutoScroll() {
        autoScrollJob?.cancel()
        autoScrollJob = null
    }
}