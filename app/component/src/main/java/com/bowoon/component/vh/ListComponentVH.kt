package com.bowoon.component.vh

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.GridSpacingItemDecoration
import com.bowoon.commonutils.LoadMore
import com.bowoon.commonutils.RecyclerViewScrollEventListener
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.component.adapters.PokemonPagingAdapter
import com.bowoon.component.adapters.PokemonPagingAdapterTemp
import com.bowoon.component.base.BaseComponentVH
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.VhListComponentBinding
import com.bowoon.component.ui.MainVM
import kotlinx.coroutines.launch

class ListComponentVH(
    private val binding: VhListComponentBinding,
    private val vm: MainVM
) : BaseComponentVH<Components.ListComponent>(binding) {
    override fun bind(component: Components.ListComponent?) {
        component?.let {
            binding.apply {
                rvListComponent.apply {
                    layoutParams.apply {
                        width = if (it.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                            ViewGroup.LayoutParams.MATCH_PARENT
                        } else if (it.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        } else {
                            it.width?.dp
                        } ?: 0
                        height = if (it.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                            ViewGroup.LayoutParams.MATCH_PARENT
                        } else if (it.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        } else {
                            it.height?.dp
                        } ?: 0
                    }

                    if (itemDecorationCount == 0) {
                        addItemDecoration(GridSpacingItemDecoration((it.spanCount ?: 1).dp, (it.betweenMargin ?: 0).dp))
                    }
                    layoutManager = GridLayoutManager(binding.root.context, it.spanCount ?: 1, it.orientation ?: RecyclerView.VERTICAL, false)
                    adapter = PokemonPagingAdapter().apply {
                        (root.context as? FragmentActivity)?.let { fa ->
                            fa.lifecycleScope.launch {
                                vm.pokemonPageFlow.collect { pagingData ->
                                    submitData(pagingData)
                                }
                            }
                        }
                    }
//                    adapter = PokemonPagingAdapterTemp().apply {
//                        submitList()
//                    }
//                    clearOnScrollListeners()
//                    addOnScrollListener(
//                        RecyclerViewScrollEventListener(
//                            object : LoadMore {
//                                override fun loadMore() {
//                                    (adapter as? PokemonPagingAdapterTemp)?.submitList()
//                                }
//                            }
//                        )
//                    )
                }
            }
        }
    }
}