package com.lol.ui

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.data.base.util.DataStatus
import com.data.base.util.Log
import com.data.base.util.ScreenUtils.dp
import com.data.base.util.ViewAdapter.loadImage
import com.domain.lol.dto.ChampionInfo
import com.lol.R
import com.lol.base.BaseFragment
import com.lol.databinding.FragmentChampionDetailBinding
import com.lol.ui.vm.ChampionDetailVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
                            binding?.champion = it.data.data?.get(key)
                            binding?.version = it.data.version
                            it.data.data?.get(key)?.getChampionSkinImageUrlList()?.forEach { url ->
                                binding?.llcChampionSkinGroup?.addView(
                                    AppCompatImageView(requireContext()).apply {
                                        layoutParams = ViewGroup.LayoutParams(
                                            ViewGroup.LayoutParams.MATCH_PARENT,
                                            ViewGroup.LayoutParams.MATCH_PARENT
                                        )
                                        loadImage(url)
                                    }
                                )
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