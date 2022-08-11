package com.lol.util

import androidx.viewpager2.widget.ViewPager2
import com.lol.ui.adapter.InfiniteViewPagerAdapter

object ViewPagerUtils {
    fun <V : List<Any?>> ViewPager2.infiniteViewPager2(
        items: V? = null,
//        fragmentVM: ViewModel? = null,
//        activityVM: ViewModel? = null,
//        displayCountView: TextView? = null,
//        separator: String? = "/",
//        indicatorView: TabLayout? = null
    ) {
        if (items.isNullOrEmpty()) {
            return
        }

        this.adapter = InfiniteViewPagerAdapter().apply {
            this.items = items
        }

//        if (items.size == 1) {
//            displayCountView?.isVisible = false
//            indicatorView?.isVisible = false
//        } else {
//            displayCountView?.isVisible = true
//            indicatorView?.isVisible = true
//        }
//
//        displayCountView?.text = String.format("%d %s %d", 0, separator, items.size)
//
//        offscreenPageLimit = 3
//
//        indicatorView?.also {
//            it.removeAllTabs()
//
//            for (item in 0 until items.size) {
//                val tab = it.newTab()
//                it.addTab(tab, false)
//            }
//        }

        post {
            currentItem = 1
        }

        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (positionOffset > 0.1) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

//                val index = if (position >= items.size) items.lastIndex else position - 1
//
//                displayCountView?.text = String.format("%d %s %d", index + 1, separator, items.size)
//                indicatorView?.selectTab(indicatorView.getTabAt(index))
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)

                if (state == ViewPager2.SCROLL_STATE_IDLE || state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    if (currentItem == 0) {
                        setCurrentItem((adapter?.itemCount ?: 0) - 2, false)
                    } else if (currentItem == (adapter?.itemCount ?: 0) - 1) {
                        setCurrentItem(1, false)
                    }
                }
            }
        })
    }
}