package com.bowoon.lol.ui.vh

import androidx.databinding.ViewDataBinding
import com.bowoon.commonutils.Log
import com.bowoon.lol.base.BaseFragment
import com.bowoon.lol.base.BaseVH
import com.bowoon.lol.data.Champion
import com.bowoon.lol.databinding.VhChampionBinding

class ChampionVH(
    override val binding: VhChampionBinding,
    private val handler: BaseFragment<out ViewDataBinding>.ClickHandler? = null
) : BaseVH<VhChampionBinding, Champion>(binding) {
    override fun bind(item: Champion?) {
        runCatching {
            item ?: run {
                Log.e("ChampionVH item is null")
                return
            }
        }.onSuccess { championInfo ->
            binding.apply {
                vh = this@ChampionVH
                champion = championInfo
            }
        }.onFailure { e ->
            Log.printStackTrace(e)
        }
    }

    fun onClick(championInfo: Champion) {
        Log.d("${championInfo.name} clicked")
        handler?.showChampionDetail(championInfo)
    }
}