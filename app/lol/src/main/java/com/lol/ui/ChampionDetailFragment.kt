package com.lol.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.data.base.util.DataStatus
import com.data.base.util.Log
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
                        binding?.champion = (it as? DataStatus.Success)?.data?.data?.values?.first()
                        binding?.executePendingBindings()
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }
}