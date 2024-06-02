package com.bowoon.component.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.Log
import com.bowoon.commonutils.scrollPercent
import com.bowoon.component.R
import com.bowoon.component.adapters.ComponentAdapter
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.ActivityMainBinding
import com.bowoon.component.vh.ListComponentVH
import dagger.hilt.android.AndroidEntryPoint
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

    private fun initBinding() {
        binding.rvComponentList.apply {
            clearOnScrollListeners()
            addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val data = (binding.rvComponentList.adapter as? ComponentAdapter)?.currentList
                        val position = (binding.rvComponentList.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition() ?: 0
                        (data?.get(position) as? Components.ListComponent)?.let {
                            if (it.orientation == RecyclerView.VERTICAL) {
                                val vh = binding.rvComponentList.findViewHolderForAdapterPosition(position) as? ListComponentVH
                                (binding.rvComponentList.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()?.let {
                                    if (binding.rvComponentList.findViewHolderForAdapterPosition(it) !is ListComponentVH) {
                                        vh?.scrollEventListener(true)
                                    }
                                }
                                Log.d(TAG, "${scrollPercent(binding.rvComponentList)}")
                                if ((binding.rvComponentList.adapter?.itemCount ?: 0) == data.size && scrollPercent(binding.rvComponentList) > 95f) {
                                    vh?.scrollEventListener(true)
                                }
                            }
                        }
                    }
                }
            )
        }
    }

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