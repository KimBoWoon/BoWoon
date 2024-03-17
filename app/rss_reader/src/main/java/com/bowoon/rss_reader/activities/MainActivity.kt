package com.bowoon.rss_reader.activities

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.ViewPager2
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.commonutils.ViewAdapter.onDebounceClickListener
import com.bowoon.commonutils.recyclerView
import com.bowoon.rss_reader.R
import com.bowoon.rss_reader.activities.vm.MainVM
import com.bowoon.rss_reader.adapter.RssAdapter
import com.bowoon.rss_reader.base.BaseActivity
import com.bowoon.rss_reader.databinding.ActivityMainBinding
import com.bowoon.rss_reader.databinding.DialogSaveRssBinding
import com.bowoon.rss_reader.vh.RssContentVH
import com.bowoon.ui.CustomViewDialog
import com.bowoon.ui.YesOrNoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val viewModel by viewModels<MainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            vpRssList.apply {
                adapter = RssAdapter()
                offscreenPageLimit = 3

                if (itemDecorationCount == 0) {
                    addItemDecoration(object : ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            val size = (parent.adapter?.itemCount ?: 0) - 1
                            val position = parent.getChildAdapterPosition(view)

                            when (position) {
                                0 -> {
                                    outRect.top = 20.dp
                                    outRect.bottom = 50.dp
                                }
                                size -> {
                                    outRect.top = 50.dp
                                    outRect.bottom = 20.dp
                                }
                                else -> {
                                    outRect.top = 50.dp
                                    outRect.bottom = 50.dp
                                }
                            }
                        }
                    })
                }

                val pageTranslationY = 25.dp + 50.dp
                setPageTransformer { page, position ->
                    page.translationY = -pageTranslationY * position
                }

                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        (vpRssList.recyclerView.findViewHolderForAdapterPosition(position) as? RssContentVH)?.startAutoScroll()
                    }
                    override fun onPageScrollStateChanged(state: Int) {
                        super.onPageScrollStateChanged(state)
                        when (state) {
                            ViewPager2.SCROLL_STATE_DRAGGING -> {
                                (vpRssList.recyclerView.layoutManager as? LinearLayoutManager)?.let { manager ->
                                    (vpRssList.recyclerView.findViewHolderForAdapterPosition(manager.findFirstCompletelyVisibleItemPosition()) as? RssContentVH)?.stopAutoScroll()
                                }
                            }
                            ViewPager2.SCROLL_STATE_IDLE -> {}
                            ViewPager2.SCROLL_STATE_SETTLING -> {}
                        }
                    }
                })
            }
            bGoToSearch.onDebounceClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
            bSaveRss.onDebounceClickListener {
                CustomViewDialog.newInstance<DialogSaveRssBinding>(
                    R.layout.dialog_save_rss,
                    { dialogBinding ->
                        dialogBinding.apply {
                            bSave.onDebounceClickListener {
                                val address = etRssAddress.text.toString()
                                Log.d(TAG, address)
                                dismissAllowingStateLoss()
                            }
                            bCancel.onDebounceClickListener {
                                dismissAllowingStateLoss()
                            }
                        }
                    },
                    isCancelable = false,
                    isCanceledOnTouchOutside = false
                ).show(this@MainActivity)
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.rss.collectLatest {
                    when (it) {
                        is DataStatus.Loading -> {
                            binding.pbLoading.isVisible = true
                        }
                        is DataStatus.Success -> {
                            binding.pbLoading.isVisible = false
                            (binding.vpRssList.adapter as? RssAdapter)?.submitList(it.data)
                        }
                        is DataStatus.Failure -> {
                            binding.pbLoading.isVisible = false
                            Log.printStackTrace(it.throwable)
                            YesOrNoDialog.newInstance(
                                it.throwable.message ?: "잠시후에 다시 시도해주세요.",
                                "재시도", { viewModel.fetchRss() },
                                "취소", {}
                            ).show(this@MainActivity)
                        }
                    }
                }
            }
        }
    }
}