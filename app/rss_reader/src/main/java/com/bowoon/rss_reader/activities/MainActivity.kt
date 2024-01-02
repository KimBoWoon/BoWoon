package com.bowoon.rss_reader.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.ViewAdapter.onDebounceClickListener
import com.bowoon.rss_reader.R
import com.bowoon.rss_reader.activities.vm.MainVM
import com.bowoon.rss_reader.adapter.RssListAdapter
import com.bowoon.rss_reader.base.BaseActivity
import com.bowoon.rss_reader.databinding.ActivityMainBinding
import com.bowoon.rss_reader.producer.ArticleProducer
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

    private fun initBinding() {
        binding.apply {
            vpRssList.apply {
                adapter = RssListAdapter()
            }
//            rvRssList.apply {
//                adapter = ArticleAdapter()
//                if (itemDecorationCount == 0) {
//                    addItemDecoration(
//                        StickyHeaderItemDecoration(
//                            object : StickyHeaderItemDecoration.SectionCallback {
//                                override fun isHeader(position: Int): Boolean =
//                                    (adapter as? ArticleAdapter)?.isHeader(position) ?: false
//
//                                override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
//                                    (adapter as? ArticleAdapter)?.getHeaderLayout(list, position)
//                            }
//                        )
//                    )
//                }
//                clearOnScrollListeners()
//                addOnScrollListener(
//                    RecyclerViewScrollEventListener(
//                        object : LoadMore {
//                            override fun loadMore() {
//                                lifecycleScope.launch(Dispatchers.IO) {
//                                    if (!ArticleProducer.produce.isClosedForReceive) {
//                                        val feed = ArticleProducer.produce.receive()
//                                        viewModel.fetchRss(feed.url ?: "")
//                                    }
//                                }
//                            }
//                        }
//                    )
//                )
//            }
            bGoToSearch.onDebounceClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
    }

    private fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.loadingCount.collectLatest {
                    binding.tvLoadingCount.text = String.format("%d/%d번째 데이터 로딩중...", it, ArticleProducer.feeds.size)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.rssList.collectLatest {
                    when (it) {
                        is DataStatus.Loading -> {
                            binding.pbLoading.isVisible = true
                            binding.tvLoadingCount.isVisible = true
                        }
                        is DataStatus.Success -> {
                            binding.pbLoading.isVisible = false
                            binding.tvLoadingCount.isVisible = false
                            (binding.vpRssList.adapter as? RssListAdapter)?.submitList(it.data)
                        }
                        is DataStatus.Failure -> {
                            binding.pbLoading.isVisible = false
                            binding.tvLoadingCount.isVisible = false
                            Log.printStackTrace(it.throwable)
                            YesOrNoDialog.newInstance(
                                it.throwable.message ?: "잠시후에 다시 시도해주세요.",
                                "재시도" to { viewModel.fetchRss() },
                                "취소" to {}
                            ).show(this@MainActivity)
                        }
                    }
                }
//                viewModel.rss.collect {
//                    when (it) {
//                        is DataStatus.Loading -> {
//                            binding.pbLoading.isVisible = true
//                        }
//                        is DataStatus.Success -> {
//                            binding.pbLoading.isVisible = false
//                            (binding.vpRssList.adapter as? RssListAdapter)?.submitList(it.data)
////                            (binding.rvRssList.adapter as? ArticleAdapter)?.apply {
////                                submitList(currentList + it.data)
////                            }
////                            (binding.rvRssList.adapter as? ArticleAdapter)?.addItems(it.data)
////                            RecyclerViewScrollEventListener.loading = false
//                        }
//                        is DataStatus.Failure -> {
//                            binding.pbLoading.isVisible = false
//                            YesOrNoDialog.newInstance(
//                                it.throwable.message ?: "잠시후에 다시 시도해주세요.",
//                                "재시도" to { viewModel.fetchRss() },
//                                "취소" to {}
//                            ).show(this@MainActivity)
//                        }
//                    }
//                }
            }
        }
    }
}