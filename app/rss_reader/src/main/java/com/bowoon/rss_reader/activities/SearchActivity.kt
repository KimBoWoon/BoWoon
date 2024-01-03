package com.bowoon.rss_reader.activities

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.StickyHeaderItemDecoration
import com.bowoon.commonutils.ViewAdapter.onDebounceClickListener
import com.bowoon.commonutils.ViewUtils.hideSoftKeyboard
import com.bowoon.rss_reader.R
import com.bowoon.rss_reader.activities.vm.SearchVM
import com.bowoon.rss_reader.adapter.SearchArticleAdapter
import com.bowoon.rss_reader.base.BaseActivity
import com.bowoon.rss_reader.databinding.ActivitySearchBinding
import com.bowoon.rss_reader.producer.RssProducer
import com.bowoon.ui.YesOrNoDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : BaseActivity<ActivitySearchBinding>(R.layout.activity_search) {
    private val viewModel by viewModels<SearchVM>()
    private var counter = 0
    @Inject
    lateinit var rssProducer: RssProducer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SearchActivity
        }
        lifecycle.addObserver(viewModel)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                updateCounter()
            }
        }

        initBinding()
        initFlow()
    }

    override fun onDestroy() {
        rssProducer.channelClose()
        super.onDestroy()
    }

    override fun initBinding() {
        binding.apply {
            rvSearchList.apply {
                adapter = SearchArticleAdapter()
                if (itemDecorationCount == 0) {
                    addItemDecoration(
                        StickyHeaderItemDecoration(
                            object : StickyHeaderItemDecoration.SectionCallback {
                                override fun isHeader(position: Int): Boolean =
                                    (adapter as? SearchArticleAdapter)?.isHeader(position) ?: false

                                override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
                                    (adapter as? SearchArticleAdapter)?.getHeaderLayout(list, position)
                            }
                        )
                    )
                }
            }
            etInputKeyword.setOnKeyListener { v, keyCode, event ->
                when {
                    event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER -> {
                        bDoSearch.performClick()
                        true
                    }
                    else -> {
                        Log.d("not yet key event -> ${event.action}, net yet key code -> $keyCode")
                        false
                    }
                }
            }
            bDoSearch.onDebounceClickListener {
                root.hideSoftKeyboard()
                binding.pbLoading.isVisible = true
                (binding.rvSearchList.adapter as? SearchArticleAdapter)?.submitList(emptyList())
//                (binding.rvSearchList.adapter as? ArticleAdapter)?.clear()
                viewModel.fetchRss(binding.etInputKeyword.text.toString())
                lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        rssProducer.reset()
                    }
                }
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.rss.collect {
                    when (it) {
                        is DataStatus.Loading -> {
                            binding.tvArticleCount.text = String.format("Results: %d", 0)
                        }
                        is DataStatus.Success -> {
                            binding.pbLoading.isVisible = false
                            (binding.rvSearchList.adapter as? SearchArticleAdapter)?.submitList(it.data)
//                        (binding.rvSearchList.adapter as? ArticleAdapter)?.addItems(it.data)
                            binding.tvArticleCount.text = String.format("Results: %d", it.data.filter { !it.isHeader }.size)
//                        binding.tvArticleCount.text = String.format("Results: %d", counter)
                        }
                        is DataStatus.Failure -> {
                            binding.pbLoading.isVisible = false
                            YesOrNoDialog.newInstance(
                                it.throwable.message ?: "잠시후에 다시 시도해주세요.",
                                "재시도", { viewModel.fetchRss(binding.etInputKeyword.text.toString()) },
                                "취소", {}
                            ).show(this@SearchActivity)
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateCounter() {
        val notifications = rssProducer.getNotificationChannel()

        while (!notifications.isClosedForReceive) {
            val action = notifications.receive()

            withContext(Dispatchers.Main) {
                when (action) {
                    RssProducer.Action.INCREASE -> counter++
                    RssProducer.Action.RESET -> counter = 0
                }
                binding.tvArticleCount.text = String.format("Results: %d", counter)
            }
        }
    }
}