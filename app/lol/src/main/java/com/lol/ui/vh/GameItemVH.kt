package com.lol.ui.vh

import androidx.databinding.ViewDataBinding
import com.bowoon.lol.databinding.VhGameItemBinding
import com.domain.lol.dto.GameItemInfo
import com.lol.base.BaseFragment
import com.lol.base.BaseVH
import com.data.util.Log

class GameItemVH(
    override val binding: VhGameItemBinding,
    private val handler: BaseFragment<out ViewDataBinding>.ClickHandler? = null
) : BaseVH<VhGameItemBinding, GameItemInfo>(binding) {
    override fun bind(item: GameItemInfo?) {
        runCatching {
            item ?: run {
                Log.e("GameItemVH item is null!")
                return
            }
        }.onSuccess { gameItemInfo ->
            binding.apply {
                vh = this@GameItemVH
                gameItem = gameItemInfo
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun onClick(gameItemInfo: GameItemInfo) {
        handler?.showGameItemDetail(gameItemInfo)
    }
}