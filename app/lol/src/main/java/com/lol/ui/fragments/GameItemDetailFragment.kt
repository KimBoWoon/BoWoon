package com.lol.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.domain.lol.dto.GameItemInfo
import com.lol.R
import com.lol.base.BaseFragment
import com.lol.databinding.FragmentGameItemDetailBinding
import com.lol.ui.fragments.vm.GameItemDetailVM
import dagger.hilt.android.AndroidEntryPoint
import util.Log

@AndroidEntryPoint
class GameItemDetailFragment : BaseFragment<FragmentGameItemDetailBinding>(
    R.layout.fragment_game_item_detail
) {
    private val viewModel by viewModels<GameItemDetailVM>()
    private var gameItem: GameItemInfo? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@GameItemDetailFragment
        }
        lifecycle.addObserver(viewModel)

        requireArguments().apply {
            gameItem = getParcelable<GameItemInfo>("gameItem") ?: run {
                Log.e("아이템 정보가 없습니다.")
                null
            }
        }

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            gameItem = this@GameItemDetailFragment.gameItem
            executePendingBindings()
        }
    }

    override fun initFlow() {

    }
}