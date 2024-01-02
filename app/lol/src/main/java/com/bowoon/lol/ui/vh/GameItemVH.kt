package com.bowoon.lol.ui.vh

import androidx.databinding.ViewDataBinding
import com.bowoon.commonutils.Log
import com.bowoon.lol.base.BaseFragment
import com.bowoon.lol.base.BaseVH
import com.bowoon.lol.data.GameItemInfo
import com.bowoon.lol.databinding.VhGameItemBinding

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