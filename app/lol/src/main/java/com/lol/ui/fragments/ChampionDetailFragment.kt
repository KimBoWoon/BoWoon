package com.lol.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.domain.lol.dto.ChampionInfo
import com.lol.R
import com.lol.base.BaseFragment
import com.lol.databinding.FragmentChampionDetailBinding
import com.lol.ui.adapter.LolAdapter
import com.lol.ui.fragments.vm.ChampionDetailVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log

@AndroidEntryPoint
class ChampionDetailFragment : BaseFragment<FragmentChampionDetailBinding>(
    R.layout.fragment_champion_detail
) {
    private val viewModel by viewModels<ChampionDetailVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@ChampionDetailFragment
        }
        lifecycle.addObserver(viewModel)

        requireArguments().apply {
            getParcelable<ChampionInfo>("championInfo")?.let { championInfo ->
                viewModel.getChampionInfo(championInfo.version ?: "", championInfo.id ?: "")
            } ?: run {
                Log.e("전달받은 챔피언 정보가 없습니다!")
            }
        }

        initBinding()
        initFlow()
    }

    override fun initBinding() {
//        TODO("Not yet implemented")
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.champion.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success -> {
                        it.data.data?.keys?.firstOrNull()?.let { key ->
                            val champion = it.data.data?.get(key)
                            binding?.champion = champion
                            binding?.version = it.data.version
                            binding?.vpChampionSkins?.apply {
                                offscreenPageLimit = champion?.skins?.size ?: 1
                                adapter = LolAdapter(champion?.skins, championName = key)
                            }
                            binding?.vpChampionSpells?.apply {
                                offscreenPageLimit = champion?.skins?.size ?: 1
                                adapter = LolAdapter(champion?.spells)
                            }
                            binding?.executePendingBindings()
                        } ?: run {
                            Log.e("champion not found")
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