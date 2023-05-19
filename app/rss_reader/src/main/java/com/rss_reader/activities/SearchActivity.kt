package com.rss_reader.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.rss_reader.R
import com.rss_reader.activities.vm.SearchVM
import com.rss_reader.adapter.ArticleAdapter
import com.rss_reader.adapter.StickyDecoration
import com.rss_reader.base.BaseActivity
import com.rss_reader.databinding.ActivitySearchBinding
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import util.DataStatus
import util.StickyHeaderItemDecoration
import util.ViewAdapter.onDebounceClickListener
import util.ViewUtils.hideSoftKeyboard

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {
    private val viewModel by viewModels<SearchVM>()
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SearchActivity
        }
        lifecycle.addObserver(viewModel)

        lifecycleScope.launchWhenStarted {
            updateCounter()
        }

        initBinding()
        initFlow()
    }

    private fun initBinding() {
        binding.apply {
            rvSearchList.apply {
                adapter = ArticleAdapter()
                if (itemDecorationCount == 0) {
                    addItemDecoration(StickyHeaderItemDecoration(StickyDecoration(adapter)))
                }
            }
            bDoSearch.onDebounceClickListener {
                root.hideSoftKeyboard()
                binding.pbLoading.isVisible = true
                (binding.rvSearchList.adapter as? ArticleAdapter)?.submitList(emptyList())
//                (binding.rvSearchList.adapter as? ArticleAdapter)?.clear()
                viewModel.fetchRss(binding.etInputKeyword.text.toString())
                lifecycleScope.launchWhenStarted {
                    ArticleProducer.reset()
                }
            }
        }
    }

    private fun initFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.rss.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        binding.tvArticleCount.text = String.format("Results: %d", 0)
                    }
                    is DataStatus.Success -> {
                        binding.pbLoading.isVisible = false
                        (binding.rvSearchList.adapter as? ArticleAdapter)?.submitList(it.data)
//                        (binding.rvSearchList.adapter as? ArticleAdapter)?.addItems(it.data)
                        binding.tvArticleCount.text = String.format("Results: %d", it.data.filter { !it.isHeader }.size)
//                        binding.tvArticleCount.text = String.format("Results: %d", counter)
                    }
                    is DataStatus.Failure -> {
                        binding.pbLoading.isVisible = false
                        AlertDialog.Builder(this@SearchActivity)
                            .setTitle("Error!")
                            .setMessage(it.throwable.message ?: "잠시후에 다시 시도해주세요.")
                            .setPositiveButton("재시도", { dialog, which ->
                                viewModel.fetchRss(binding.etInputKeyword.text.toString())
                            }).setNegativeButton("취소", { dialog, which ->
                                dialog?.dismiss()
                            }).create().show()
                    }
                }
            }
        }
    }

    private suspend fun updateCounter() {
        val notifications = ArticleProducer.getNotificationChannel()

        while (!notifications.isClosedForReceive) {
            val action = notifications.receive()

            withContext(Dispatchers.Main) {
                when (action) {
                    ArticleProducer.Action.INCREASE -> counter++
                    ArticleProducer.Action.RESET -> counter = 0
                }
                binding.tvArticleCount.text = String.format("Results: %d", counter)
            }
        }
    }
}