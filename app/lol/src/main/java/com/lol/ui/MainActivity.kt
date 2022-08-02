package com.lol.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.data.base.util.DataStatus
import com.data.base.util.Log
import com.domain.lol.dto.ChampionDetail
import com.domain.lol.dto.ChampionInfo
import com.domain.lol.dto.ChampionRoot
import com.lol.R
import com.lol.base.BaseActivity
import com.lol.databinding.ActivityMainBinding
import com.lol.ui.adapter.ChampionAdapter
import com.lol.ui.vm.MainVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    private val viewModel by viewModels<MainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
        }

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            rvLolChampionList.apply {
                if (layoutManager == null) {
                    layoutManager = GridLayoutManager(this@MainActivity, 3).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int = 3
                        }
                    }
                }
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.lolVersion.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                        binding.pbLoading.isVisible = true
                    }
                    is DataStatus.Success -> {
                        Log.d(it.data.toString())
                        @Suppress("UNCHECKED_CAST")
                        (it.data as? List<String>)?.firstOrNull()?.let { version ->
                            viewModel.getAllChampion(version)
                        } ?: Log.e("version is null!")
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                        binding.pbLoading.isVisible = false
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.allChampion.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success -> {
                        Log.d(it.data.toString())
                        (it.data as? ChampionRoot)?.data?.let { championList ->
                            val sortedChampionList = championList.values.sortedBy { item -> item.name }
                            binding.rvLolChampionList.adapter = ChampionAdapter(sortedChampionList, ClickHandler())
                        } ?: run {
                            Log.e("championList is null")
                        }
                        binding.pbLoading.isVisible = false
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                        binding.pbLoading.isVisible = false
                    }
                }
            }
        }
    }

    inner class ClickHandler {
        fun showChampionDetail(championInfo: ChampionInfo) {
            Log.d("showChampionDetail >>>>> $championInfo")
            viewModel.getChampionInfo(championInfo.version ?: "", championInfo.id ?: "")
        }
    }
}