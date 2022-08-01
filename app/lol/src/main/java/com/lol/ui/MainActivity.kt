package com.lol.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.data.lol.util.Log
import com.data.lol.util.DataStatus
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

        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.lolVersion.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success<*> -> {
                        Log.d(it.data.toString())
                        @Suppress("UNCHECKED_CAST")
                        (it.data as? List<String>)?.firstOrNull()?.let { version ->
                            viewModel.getAllChampion(version)
                        } ?: Log.e("version is null!")
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
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
                    is DataStatus.Success<*> -> {
                        Log.d(it.data.toString())
                        (it.data as? ChampionRoot)?.data?.let { championList ->
                            binding.rvLolChampionList.adapter = ChampionAdapter(championList.getChampionList())
                        } ?: run {
                            Log.e("championList is null")
                        }
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }
}