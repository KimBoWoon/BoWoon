package com.lol.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.domain.lol.dto.ChampionInfo
import com.domain.lol.dto.Skin
import com.lol.R
import com.lol.base.BaseFragment
import com.lol.databinding.FragmentChampionDetailBinding
import com.lol.ui.adapter.LolAdapter
import com.lol.ui.fragments.vm.ChampionDetailVM
import com.lol.util.ViewPagerUtils.infiniteViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log
import util.ifNotNull

@AndroidEntryPoint
class ChampionDetailFragment : BaseFragment<FragmentChampionDetailBinding>(
    R.layout.fragment_champion_detail
) {
    private val viewModel by viewModels<ChampionDetailVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
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
                        binding.pbChampionDetailLoading.isVisible = true
                    }
                    is DataStatus.Success -> {
                        it.data.data?.keys?.firstOrNull()?.let { key ->
                            it.data.data?.get(key)?.let { championDetail ->
                                championDetail.image?.version = championDetail.version
                                binding.champion = championDetail
                                binding.vpChampionSkins.apply {
                                    championDetail.skins?.forEach { skin ->
                                        skin?.championName = key
                                    }
                                    offscreenPageLimit = championDetail.skins?.size ?: 1
                                    infiniteViewPager2(championDetail.skins)
                                }
                                binding.vpChampionSpells.apply {
                                    ifNotNull(championDetail.passive, championDetail.spells) { passive, spells ->
                                        passive.image?.version = championDetail.version
                                        mutableListOf<Any>(passive).apply {
                                            spells.forEach { spell ->
                                                spell?.let {
                                                    spell.image?.version = championDetail.version
                                                    add(spell)
                                                }
                                            }
                                        }.apply {
                                            offscreenPageLimit = size ?: 1
                                            adapter = LolAdapter(this)
                                        }
                                    }
                                }
                                binding.pbChampionDetailLoading.isVisible = false
                                binding.executePendingBindings()
                            }
                        } ?: run {
                            Log.e("champion not found")
                        }
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                        binding?.pbChampionDetailLoading.isVisible = false
                    }
                }
            }
        }
    }
}