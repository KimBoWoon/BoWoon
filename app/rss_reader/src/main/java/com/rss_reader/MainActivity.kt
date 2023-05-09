package com.rss_reader

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rss_reader.adapter.ArticleAdapter
import com.rss_reader.adapter.ArticleLoader
import com.rss_reader.databinding.ActivityMainBinding
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log
import util.ScreenUtils.dp

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
            val producer = ArticleProducer.producer

            @OptIn(ExperimentalCoroutinesApi::class)
            Log.d("isClosedForReceive > ${!producer.isClosedForReceive}")
            @OptIn(ExperimentalCoroutinesApi::class)
            if (!producer.isClosedForReceive) {
                val articles = producer.receive()

                lifecycleScope.launch {
                    binding.pbLoading.isVisible = false
                    (binding.rvRssList.adapter as? ArticleAdapter)?.submitList(articles)
                }
            }
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
                    addItemDecoration(object : RecyclerView.ItemDecoration() {
                        override fun getItemOffsets(
                            outRect: Rect,
                            view: View,
                            parent: RecyclerView,
                            state: RecyclerView.State
                        ) {
                            val position = parent.getChildLayoutPosition(view)
                            val size = parent.adapter?.itemCount ?: 0
                            if (position in 0 .. size) {
                                when (position) {
                                    0 -> {
                                        outRect.top = 10.dp
                                        outRect.bottom = 5.dp
                                    }
                                    state.itemCount - 1 -> {
                                        outRect.top = 5.dp
                                        outRect.bottom = 10.dp
                                    }
                                    else -> {
                                        outRect.top = 5.dp
                                        outRect.bottom = 5.dp
                                    }
                                }
                            }
                            outRect.left = 10.dp
                            outRect.right = 10.dp
                        }
                    })
                }
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