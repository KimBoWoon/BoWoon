package com.lol.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lol.R
import com.lol.base.BaseFragment
import com.lol.databinding.FragmentGameItemBinding
import com.lol.ui.activities.vm.MainVM
import com.lol.ui.adapter.LolAdapter
import com.lol.ui.fragments.vm.GameItemVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log

@AndroidEntryPoint
class GameItemListFragment : BaseFragment<FragmentGameItemBinding>(
    R.layout.fragment_game_item
) {
    private val activityVM by activityViewModels<MainVM>()
    private val viewModel by viewModels<GameItemVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@GameItemListFragment
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun onResume() {
        super.onResume()

        activityVM.changeVersion()
    }

    override fun initBinding() {
        binding.apply {
            rvGameItemList.apply {
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            activityVM.allGameItem.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success -> {
                        Log.d(it.data.toString())
                        it.data?.data?.let { gameItemList ->
                            val sortedGameItemList = gameItemList.values.sortedBy { item -> item.name }
                            gameItemList.values.forEach { item ->
                                item.image?.version = it.data?.version ?: ""
                            }
                            binding.rvGameItemList.adapter = LolAdapter(sortedGameItemList, handler)
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
}