package com.lol.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bowoon.lol.R
import com.bowoon.lol.databinding.FragmentGameItemBinding
import com.google.android.material.chip.Chip
import com.lol.base.BaseFragment
import com.lol.ui.activities.vm.MainVM
import com.lol.ui.adapter.LolAdapter
import com.lol.ui.fragments.vm.GameItemVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log
import util.ViewAdapter.onDebounceClickListener

@AndroidEntryPoint
class GameItemListFragment : BaseFragment<FragmentGameItemBinding>(
    R.layout.fragment_game_item
) {
    private val activityVM by activityViewModels<MainVM>()
    private val viewModel by viewModels<GameItemVM>()
    private val tags = mutableListOf<String>()
    private val clickHandler = ClickHandler()

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
                            val sortedGameItemList = gameItemList.values.filter { item -> item.inStore != false }.sortedBy { item -> item.name }
                            gameItemList.values.forEach { item ->
                                item.image?.version = it.data?.version ?: ""
                                item.tags?.let { tagList ->
                                    tagList.forEach { tag ->
                                        tag?.let {
                                            tags.add(it)
                                        }
                                    }
                                }
                            }
                            binding.rvGameItemList.adapter = LolAdapter(sortedGameItemList, handler)
                            binding.cgGameItemCategoryGroup.apply {
                                tags.distinct().sorted().forEach { tag ->
                                    addView(Chip(requireContext()).apply {
                                        text = tag
                                        isCheckable = true
                                        onDebounceClickListener {
                                            clickHandler.clickCategory(tag)
                                        }
                                    })
                                }
                            }
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
        fun clickCategory(category: String) {
            binding.rvGameItemList.adapter = LolAdapter(
                (activityVM.allGameItem.value as? DataStatus.Success)?.data?.data?.values?.filter { it.tags?.contains(category) == true },
                handler
            )
        }
    }
}