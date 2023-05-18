package com.rss_reader.activities

import android.content.Intent
import android.os.Bundle
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
import com.rss_reader.adapter.StickyHeaderItemDecoration
import com.rss_reader.databinding.ActivityMainBinding
import com.rss_reader.producer.ArticleProducer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import util.DataStatus
import util.LoadMore
import util.RecyclerViewScrollEventListener
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
    private var headerView: View? = null
//    private val callback = object : StickyHeaderItemDecoration.SectionCallback {
//        override fun isHeader(position: Int): Boolean =
//            (binding.rvRssList.adapter as? ArticleAdapter)?.currentList?.get(position)?.isHeader ?: false
//
//        override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
//            when ((list.adapter as? ArticleAdapter)?.currentList?.get(position)?.isHeader) {
//                true -> {
//                    Log.d("header view true > $position")
//                    (list.adapter as? ArticleAdapter)?.currentList?.get(position)?.let {
//                        headerView = VhFeedHeaderBinding.inflate(LayoutInflater.from(list.context), list, false).apply {
//                            rss = it
//                            executePendingBindings()
//                        }.root
//                        headerView
//                    }
//                }
//                else -> {
//                    Log.d("header view false > $position")
//                    headerView
//                }
//            }
//    }
    private val callback = object : StickyHeaderItemDecoration.SectionCallback {
        override fun isHeader(position: Int): Boolean =
            (binding.rvRssList.adapter as? ArticleAdapter)?.isHeader(position) ?: false

        override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? =
            (binding.rvRssList.adapter as? ArticleAdapter)?.getHeaderLayout(list, position)
    }

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
                adapter = ArticleAdapter(emptyList())
                if (itemDecorationCount == 0) {
                    addItemDecoration(StickyHeaderItemDecoration(callback))
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
//                        (binding.rvRssList.adapter as? ArticleAdapter)?.submitList(it.data)
                        (binding.rvRssList.adapter as? ArticleAdapter)?.addItems(it.data)
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