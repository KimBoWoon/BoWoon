package com.rss_reader.activities

import android.content.Intent
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
import com.rss_reader.activities.vm.MainVM
import com.rss_reader.adapter.ArticleAdapter
import com.rss_reader.adapter.ArticleLoader
import com.rss_reader.adapter.StickyHeaderItemDecoration
import com.rss_reader.databinding.ActivityMainBinding
import com.rss_reader.databinding.VhFeedNameBinding
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log
import util.ViewAdapter.onDebounceClickListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(
            this@MainActivity,
            R.layout.activity_main
        )
    }
    private val viewModel by viewModels<MainVM>()
    private val loadMore = object : ArticleLoader {
        override suspend fun loadMore() {
            runCatching {
                val producer = ArticleProducer.producer

                @OptIn(ExperimentalCoroutinesApi::class)
                if (!producer.isClosedForReceive) {
                    val articles = producer.receive()

                    lifecycleScope.launch {
                        binding.pbLoading.isVisible = false
                        (binding.rvRssList.adapter as? ArticleAdapter)?.let {
                            val newList = it.currentList + articles
                            it.submitList(newList)
                        }
                    }
                }
            }.onFailure {
                Log.e(it.message ?: "something wrong...")
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Error!")
                    .setMessage(it.message ?: "잠시후에 다시 시도해주세요.")
                    /*.setPositiveButton("재시도", { dialog, which ->
                    })*/.setNegativeButton("취소", { dialog, which ->
                        dialog?.dismiss()
                    }).create().show()
            }
        }
    }
    private val callback = object : StickyHeaderItemDecoration.SectionCallback {
        override fun isHeader(position: Int): Boolean =
            (binding.rvRssList.adapter as? ArticleAdapter)?.currentList?.get(position)?.isHeader
                ?: false

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
            lifecycleOwner = this@MainActivity
        }
        lifecycle.addObserver(viewModel)

        initBinding()
//        initFlow()

        lifecycleScope.launch {
            loadMore.loadMore()
        }
    }

    private fun initBinding() {
        binding.apply {
            rvRssList.apply {
                adapter = ArticleAdapter(loadMore)
                if (itemDecorationCount == 0) {
                    addItemDecoration(StickyHeaderItemDecoration(callback))
                }
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
                        (binding.rvRssList.adapter as? ArticleAdapter)?.submitList(it.data)
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