package com.rss_reader.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.rss_reader.R
import com.rss_reader.activities.vm.MainVM
import com.rss_reader.adapter.ArticleAdapter
import com.rss_reader.adapter.StickyDecoration
import com.rss_reader.base.BaseActivity
import com.rss_reader.databinding.ActivityMainBinding
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import util.DataStatus
import util.LoadMore
import util.RecyclerViewScrollEventListener
import util.StickyHeaderItemDecoration
import util.ViewAdapter.onDebounceClickListener

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
            rvRssList.apply {
                adapter = ArticleAdapter()
                if (itemDecorationCount == 0) {
                    addItemDecoration(StickyHeaderItemDecoration(StickyDecoration(adapter)))
                }
                clearOnScrollListeners()
                val listener = RecyclerViewScrollEventListener(
                    object : LoadMore {
                        override fun loadMore() {
                            CoroutineScope(Dispatchers.IO).launch {
                                if (!ArticleProducer.produce.isClosedForReceive) {
                                    val feed = ArticleProducer.produce.receive()
                                    viewModel.fetchRss(feed.url ?: "")
                                }
                            }
                        }
                    }
                )
                addOnScrollListener(listener)
            }
            bGoToSearch.onDebounceClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
        }
    }

    private fun initFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.rss.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        binding.pbLoading.isVisible = true
                    }
                    is DataStatus.Success -> {
                        binding.pbLoading.isVisible = false
                        (binding.rvRssList.adapter as? ArticleAdapter)?.apply {
                            submitList(currentList + it.data)
                        }
//                        (binding.rvRssList.adapter as? ArticleAdapter)?.addItems(it.data)
                        RecyclerViewScrollEventListener.loading = false
                    }
                    is DataStatus.Failure -> {
                        binding.pbLoading.isVisible = false
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Error!")
                            .setMessage(it.throwable.message ?: "잠시후에 다시 시도해주세요.")
                            .setPositiveButton("재시도", { dialog, which ->
                                viewModel.fetchRss()
                            }).setNegativeButton("취소", { dialog, which ->
                                dialog?.dismiss()
                            }).create().show()
                    }
                }
            }
        }
    }
}