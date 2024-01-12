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
import com.bowoon.rss_reader.adapter.RssAdapter
import com.bowoon.rss_reader.base.BaseActivity
import com.bowoon.rss_reader.databinding.ActivityMainBinding
import com.bowoon.rss_reader.databinding.DialogSaveRssBinding
import com.bowoon.ui.CustomViewDialog
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

    override fun initBinding() {
        binding.apply {
            vpRssList.apply {
                adapter = RssAdapter()
            }
            bGoToSearch.onDebounceClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }
            bSaveRss.onDebounceClickListener {
                CustomViewDialog.newInstance<DialogSaveRssBinding>(
                    R.layout.dialog_save_rss,
                    { dialogBinding ->
                        dialogBinding.apply {
                            bSave.onDebounceClickListener {
                                val address = etRssAddress.text.toString()
                                Log.d(TAG, address)
                                dismissAllowingStateLoss()
                            }
                            bCancel.onDebounceClickListener {
                                dismissAllowingStateLoss()
                            }
                        }
                    },
                    isCancelable = false,
                    isCanceledOnTouchOutside = false
                ).show(this@MainActivity)
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.rss.collectLatest {
                    when (it) {
                        is DataStatus.Loading -> {
                            binding.pbLoading.isVisible = true
                        }
                        is DataStatus.Success -> {
                            binding.pbLoading.isVisible = false
                            (binding.vpRssList.adapter as? RssAdapter)?.submitList(it.data)
                        }
                        is DataStatus.Failure -> {
                            binding.pbLoading.isVisible = false
                            Log.printStackTrace(it.throwable)
                            YesOrNoDialog.newInstance(
                                it.throwable.message ?: "잠시후에 다시 시도해주세요.",
                                "재시도", { viewModel.fetchRss() },
                                "취소", {}
                            ).show(this@MainActivity)
                        }
                    }
                }
            }
        }
    }
}