package com.bowoon.lol.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bowoon.commonutils.Log
import com.bowoon.lol.R
import com.bowoon.lol.base.BaseFragment
import com.bowoon.lol.data.GameItemInfo
import com.bowoon.lol.databinding.FragmentGameItemDetailBinding
import com.bowoon.lol.ui.activities.vm.MainVM
import com.bowoon.lol.ui.adapter.RelatedItemAdapter
import com.bowoon.lol.ui.fragments.vm.GameItemDetailVM
import com.bowoon.commonutils.DataStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameItemDetailFragment : BaseFragment<FragmentGameItemDetailBinding>(
    R.layout.fragment_game_item_detail
) {
    private val viewModel by viewModels<GameItemDetailVM>()
    private val activityVM by activityViewModels<MainVM>()
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
            viewModel.relatedItem.value = DataStatus.Success(gameItem?.into?.map { relatedItemNum ->
                relatedItemNum?.let {
                    (activityVM.allGameItem.value as? DataStatus.Success)?.data?.data?.get(it)
                } ?: run {
                    return
                }
            })
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
        lifecycleScope.launch {
            viewModel.relatedItem.collect {
                when (it) {
                    is DataStatus.Loading -> {
                        Log.d("data loading...")
                    }
                    is DataStatus.Success -> {
                        binding.vpRelatedItem.adapter = RelatedItemAdapter(it.data)
                    }
                    is DataStatus.Failure -> {
                        Log.printStackTrace(it.throwable)
                    }
                }
            }
        }
    }
}