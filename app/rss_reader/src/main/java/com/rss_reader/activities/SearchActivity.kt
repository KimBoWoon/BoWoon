package com.rss_reader.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rss_reader.R
import com.rss_reader.activities.vm.SearchVM
import com.rss_reader.adapter.ArticleAdapter
import com.rss_reader.adapter.StickyHeaderItemDecoration
import com.rss_reader.databinding.ActivitySearchBinding
import com.rss_reader.databinding.VhFeedHeaderBinding
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.AndroidEntryPoint
import util.DataStatus
import util.ViewAdapter.onDebounceClickListener
import util.ViewUtils.hideSoftKeyboard

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        DataBindingUtil.setContentView(
            this@SearchActivity,
            R.layout.activity_search
        )
    }
    private val viewModel by viewModels<SearchVM>()
//    private val callback = object : StickyHeaderItemDecoration.SectionCallback {
//        override fun isHeader(position: Int): Boolean =
//            (binding.rvSearchList.adapter as? ArticleAdapter)?.currentList?.get(position)?.isHeader ?: false
//
//        override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
//            (list.adapter as? ArticleAdapter)?.currentList?.get(position)?.let {
//                VhFeedHeaderBinding.inflate(LayoutInflater.from(list.context), list, false).apply {
//                    rss = it
//                }.root
//            }
//    }
    private val callback = object : StickyHeaderItemDecoration.SectionCallback {
        override fun isHeader(position: Int): Boolean =
            (binding.rvSearchList.adapter as? ArticleAdapter)?.isHeader(position) ?: false

        override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
            (binding.rvSearchList.adapter as? ArticleAdapter)?.getHeaderLayout(list, position)
    }
    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SearchActivity
        }
        lifecycle.addObserver(viewModel)

        lifecycleScope.launchWhenStarted {
//            updateCounter()
        }

        initBinding()
        initFlow()
    }

    private fun initBinding() {
        binding.apply {
            rvSearchList.apply {
                adapter = ArticleAdapter()
                if (itemDecorationCount == 0) {
                    addItemDecoration(StickyHeaderItemDecoration(callback))
                }
            }
            bDoSearch.onDebounceClickListener {
                root.hideSoftKeyboard()
                binding.pbLoading.isVisible = true
//                (binding.rvSearchList.adapter as? ArticleAdapter)?.submitList(emptyList())
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
                        binding.tvArticleCount.text = "Results: 0"
                    }
                    is DataStatus.Success -> {
                        binding.pbLoading.isVisible = false
//                        (binding.rvSearchList.adapter as? ArticleAdapter)?.submitList(it.data)
                        binding.tvArticleCount.text = "Results: ${it.data.size}"
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

//    private suspend fun updateCounter() {
//        val notifications = ArticleProducer.getNotificationChannel()
//
//        while (!notifications.isClosedForReceive) {
//            val action = notifications.receive()
//
//            withContext(Dispatchers.Main) {
//                when (action) {
//                    ArticleProducer.Action.INCREASE -> counter++
//                    ArticleProducer.Action.RESET -> counter = 0
//                }
//                binding.tvArticleCount.text = "Results: $counter"
//            }
//        }
//    }
}