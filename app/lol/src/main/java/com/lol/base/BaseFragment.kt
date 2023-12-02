package com.lol.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bowoon.lol.R
import com.domain.lol.dto.ChampionInfo
import com.domain.lol.dto.GameItemInfo
import com.data.util.Log

abstract class BaseFragment<V : ViewDataBinding>(
    @LayoutRes private val layoutId: Int
) : Fragment() {
    protected lateinit var binding: V
    protected val handler = ClickHandler()

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DataBindingUtil.inflate<V>(inflater, layoutId, container, false)?.apply {
            binding = this
        } ?: run {
            throw RuntimeException("BaseFragment onCreateView layout inflater error!")
        }
        return binding.root
    }

    abstract fun initBinding()
    abstract fun initFlow()

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

        fun showGameItemDetail(gameItemInfo: GameItemInfo) {
            Log.d("showChampionDetail >>>>> $gameItemInfo")
            findNavController().navigate(
                R.id.action_gameItemListFragment_to_gameItemDetailFragment,
                Bundle().apply {
                    putParcelable("gameItem", gameItemInfo)
                }
            )
        }
    }
}