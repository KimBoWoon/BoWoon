package com.lol.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.data.base.util.DataStatus
import com.data.base.util.Log
import com.domain.lol.dto.ChampionInfo
import com.domain.lol.dto.ChampionRoot
import com.lol.R
import com.lol.base.BaseFragment
import com.lol.databinding.FragmentChampionListBinding
import com.lol.ui.adapter.ChampionAdapter
import com.lol.ui.vm.ChampionListVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChampionListFragment : BaseFragment<FragmentChampionListBinding>(
    R.layout.fragment_champion_list
) {
    private val viewModel by viewModels<ChampionListVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@ChampionListFragment
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding?.apply {
            rvLolChampionList.apply {
                if (layoutManager == null) {
                    layoutManager = GridLayoutManager(requireContext(), 3).apply {
                        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int = 3
                        }
                    }
                }
            }
//            etSearchChampion.apply {
//                addTextChangedListener { editable ->
//                    editable?.let {
//                        Log.d("editable not null!")
//                        Log.d(it.toString())
//                    }
//                }
//            }
            etSearchChampion.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(text: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    text?.let {
                        Log.d("changedText >>>>> $it")

                        val sortedChampionList = (viewModel.allChampion.value as? DataStatus.Success)?.data?.data?.values
                            ?.filter { champion ->
                                champion.name?.contains("$it", true) == true
                            }?.toList()?.sortedBy { it.name }

                        Log.d(sortedChampionList.toString())

                        binding?.rvLolChampionList?.adapter = ChampionAdapter(sortedChampionList, ClickHandler())
                    }
                }

                override fun afterTextChanged(text: Editable?) {}
            })
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.lolVersion.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                        binding?.pbLoading?.isVisible = true
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
                        binding?.pbLoading?.isVisible = false
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
                            binding?.rvLolChampionList?.adapter = ChampionAdapter(sortedChampionList, ClickHandler())
                        } ?: run {
                            Log.e("championList is null")
                        }
                        binding?.pbLoading?.isVisible = false
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                        binding?.pbLoading?.isVisible = false
                    }
                }
            }
        }
    }

    inner class ClickHandler {
        fun showChampionDetail(championInfo: ChampionInfo) {
            Log.d("showChampionDetail >>>>> $championInfo")
            findNavController().navigate(
                R.id.action_championListFragment_to_championDetailFragment,
                Bundle().apply {
                    putParcelable("championInfo", championInfo)
                }
            )
        }
    }
}