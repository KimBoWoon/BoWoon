package com.lol.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.domain.lol.dto.ChampionInfo
import com.domain.lol.dto.ChampionRoot
import com.lol.R
import com.lol.base.BaseFragment
import com.lol.databinding.FragmentChampionListBinding
import com.lol.ui.activities.vm.MainVM
import com.lol.ui.adapter.LolAdapter
import com.lol.ui.fragments.vm.ChampionListVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log

@AndroidEntryPoint
class ChampionListFragment : BaseFragment<FragmentChampionListBinding>(
    R.layout.fragment_champion_list
) {
    private val activityVM by activityViewModels<MainVM>()
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
            // TODO 해당 코드 작동 안함 이유를 모르겠음 찾아봐야함
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

                        val sortedChampionList = (activityVM.allChampion.value as? DataStatus.Success)?.data?.data?.values
                            ?.filter { champion ->
                                champion.name?.contains("$it", true) == true
                            }?.toList()?.sortedBy { it.name }

                        Log.d(sortedChampionList.toString())

                        binding?.rvLolChampionList?.adapter = LolAdapter(sortedChampionList, ClickHandler())
                    }
                }

                override fun afterTextChanged(text: Editable?) {}
            })
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            activityVM.allChampion.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success -> {
                        Log.d(it.data.toString())
                        (it.data as? ChampionRoot)?.data?.let { championList ->
                            val sortedChampionList = championList.values.sortedBy { item -> item.name }
                            binding?.rvLolChampionList?.adapter = LolAdapter(sortedChampionList, ClickHandler())
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