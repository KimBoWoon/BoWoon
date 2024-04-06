package com.bowoon.component.vh

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.commonutils.GridSpacingItemDecoration
import com.bowoon.commonutils.ScreenUtils.dp
import com.bowoon.component.adapters.PokemonPagingAdapter
import com.bowoon.component.data.Components
import com.bowoon.component.databinding.VhListComponentBinding
import com.bowoon.component.ui.MainVM
import kotlinx.coroutines.launch

class ListComponentVH(
    private val binding: VhListComponentBinding,
    private val vm: MainVM
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(content: Components.ListComponent?) {
        content?.let {
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
                        addItemDecoration(GridSpacingItemDecoration((content.spanCount ?: 1).dp, (content.betweenMargin ?: 0).dp) /*{
                            override fun getItemOffsets(
                                outRect: Rect,
                                view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State
                            ) {
                                val position = parent.getChildAdapterPosition(view)
                                val size = parent.adapter?.itemCount ?: 0

                                when (position) {
                                    0 -> {
                                        outRect.left = it.startMargin ?: 0
                                        outRect.right = if (it.betweenMargin != null && it.betweenMargin != 0) it.betweenMargin / 2 else 0
                                    }
                                    size - 1 -> {
                                        outRect.left = if (it.betweenMargin != null && it.betweenMargin != 0) it.betweenMargin / 2 else 0
                                        outRect.right = it.endMargin ?: 0
                                    }
                                    else -> {
                                        outRect.left = if (it.betweenMargin != null && it.betweenMargin != 0) it.betweenMargin / 2 else 0
                                        outRect.right = if (it.betweenMargin != null && it.betweenMargin != 0) it.betweenMargin / 2 else 0
                                    }
                                }
                            }
                        }*/)
                    }
                    layoutManager = GridLayoutManager(binding.root.context, content.spanCount ?: 1, content.orientation ?: RecyclerView.VERTICAL, false)
                    adapter = PokemonPagingAdapter().apply {
                        (root.context as? FragmentActivity)?.let { fa ->
                            fa.lifecycleScope.launch {
                                vm.pokemonPageFlow.collect { pagingData ->
                                    submitData(pagingData)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}