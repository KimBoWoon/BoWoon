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
import com.rss_reader.databinding.VhFeedNameBinding
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
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
    private val callback = object : StickyHeaderItemDecoration.SectionCallback {
        override fun isHeader(position: Int): Boolean =
            (binding.rvSearchList.adapter as? ArticleAdapter)?.currentList?.get(position)?.isHeader ?: false

        override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
            (list.adapter as? ArticleAdapter)?.currentList?.get(position)?.let {
                VhFeedNameBinding.inflate(LayoutInflater.from(list.context), list, false).apply {
                    tvFeedName.text = it.feed
                }.root
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SearchActivity
        }
        lifecycle.addObserver(viewModel)

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
                lifecycleScope.launchWhenStarted {
                    binding.pbLoading.isVisible = true
                    (binding.rvSearchList.adapter as? ArticleAdapter)?.submitList(emptyList())
                    root.hideSoftKeyboard()
                    viewModel.fetchRss(binding.etInputKeyword.text.toString())
//                    search()
                }
            }
        }
    }

    private fun initFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.rss.collect {
                when (it) {
                    is DataStatus.Loading -> {
//                        binding.pbLoading.isVisible = true
                    }
                    is DataStatus.Success -> {
                        binding.pbLoading.isVisible = false
                        (binding.rvSearchList.adapter as? ArticleAdapter)?.submitList(it.data)
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

    private suspend fun search() {
        val query = binding.etInputKeyword.text.toString()

        val channel = ArticleProducer.search(query)

        @OptIn(ExperimentalCoroutinesApi::class)
        while (!channel.isClosedForReceive) {
            val articles = channel.receive()

            lifecycleScope.launch(Dispatchers.Main) {
                binding.pbLoading.isVisible = false
                (binding.rvSearchList.adapter as? ArticleAdapter)?.let {
                    val newList = it.currentList + articles
                    it.submitList(newList)
                }
            }
        }
    }
}