package com.rss_reader.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.rss_reader.R
import com.rss_reader.activities.vm.SearchVM
import com.rss_reader.adapter.ArticleAdapter
import com.rss_reader.databinding.ActivitySearchBinding
import com.rss_reader.producer.ArticleProducer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import util.ViewAdapter.onDebounceClickListener
import util.ViewUtils.hideSoftKeyboard

class SearchActivity : AppCompatActivity() {
    private val binding: ActivitySearchBinding by lazy {
        DataBindingUtil.setContentView(
            this@SearchActivity,
            R.layout.activity_search
        )
    }
    private val viewModel by viewModels<SearchVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@SearchActivity
        }
        lifecycle.addObserver(viewModel)

        initBinding()
//        initFlow()
    }

    private fun initBinding() {
        binding.apply {
            rvSearchList.adapter = ArticleAdapter()
            bDoSearch.onDebounceClickListener {
                lifecycleScope.launchWhenStarted {
                    binding.pbLoading.isVisible = true
                    (binding.rvSearchList.adapter as? ArticleAdapter)?.submitList(emptyList())
                    root.hideSoftKeyboard()
                    search()
                }
            }
        }
    }

    private fun initFlow() {

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