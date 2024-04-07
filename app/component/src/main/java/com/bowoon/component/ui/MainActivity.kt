package com.bowoon.component.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.LoadMore
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.RecyclerViewScrollEventListener
import com.bowoon.commonutils.scrollPercent
import com.bowoon.component.R
import com.bowoon.component.adapters.ComponentAdapter
import com.bowoon.component.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "component_main_activity"
    }

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }
    private val viewModel by viewModels<MainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
        }
        lifecycle.addObserver(viewModel)

        viewModel.fetchComponent(this@MainActivity)

        initBinding()
        initFlow()
    }

    private fun initBinding() {}

    private fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.componentData.collect { componentData ->
                    when (componentData) {
                        is DataStatus.Loading -> {
                            binding.pbLoading.isVisible = true
                            Log.d(TAG, "component data loading...")
                        }
                        is DataStatus.Success -> {
                            binding.pbLoading.isVisible = false
                            binding.rvComponentList.adapter = ComponentAdapter(viewModel).apply {
                                submitList(componentData.data)
                            }
                        }
                        is DataStatus.Failure -> {
                            binding.pbLoading.isVisible = false
                            Log.printStackTrace(componentData.throwable)
                        }
                    }
                }
            }
        }
    }
}