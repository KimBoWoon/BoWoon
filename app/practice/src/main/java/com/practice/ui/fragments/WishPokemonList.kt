package com.practice.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practice.ui.activities.vm.MainVM
import com.practice.R
import com.practice.base.BaseFragment
import com.practice.databinding.FragmentWishPokemonBinding
import com.practice.paging.adapters.PokemonLoadPagingAdapter
import com.practice.paging.adapters.WishPokemonPagingAdapter
import com.practice.ui.fragments.vm.WishPokemonListVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import util.ScreenUtils.dp

@AndroidEntryPoint
class WishPokemonList : BaseFragment<FragmentWishPokemonBinding>(
    R.layout.fragment_wish_pokemon,
) {
    private val pokemonAdapter by lazy {
        WishPokemonPagingAdapter().apply {
            addLoadStateListener {
                if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && (binding?.rvWishPokemonList?.adapter?.itemCount ?: 0) < 1) {
                    binding?.rvWishPokemonList?.isVisible = false
                    binding?.tvEmpty?.isVisible = true
                    binding?.pbLoading?.isVisible = false
                } else {
                    binding?.rvWishPokemonList?.isVisible = true
                    binding?.tvEmpty?.isVisible = false
                    binding?.pbLoading?.isVisible = false
                }
                when (it.append) {
                    is LoadState.Loading -> {
                        binding?.pbLoading?.isVisible = true
                    }
                    is LoadState.NotLoading -> {
                        binding?.pbLoading?.isVisible = false
                    }
                    is LoadState.Error -> {
                        binding?.pbLoading?.isVisible = false
                    }
                }
                when (it.refresh) {
                    is LoadState.Loading -> {
                        binding?.pbLoading?.isVisible = true
                    }
                    is LoadState.Error -> {
                        binding?.pbLoading?.isVisible = false
                    }
                    is LoadState.NotLoading -> {
                        binding?.pbLoading?.isVisible = false
                    }
                }
            }
        }
    }
    private val viewModel by viewModels<WishPokemonListVM>()
    private val activityVM by activityViewModels<MainVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@WishPokemonList
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding?.rvWishPokemonList?.apply {
            adapter = pokemonAdapter.withLoadStateHeaderAndFooter(
                PokemonLoadPagingAdapter { pokemonAdapter.retry() },
                PokemonLoadPagingAdapter { pokemonAdapter.retry() }
            )
            layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        if (pokemonAdapter.getItemViewType(position) == R.layout.viewholder_load) {
                            2
                        } else {
                            1
                        }
                }
            }
            if (itemDecorationCount == 0) {
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                        val position = parent.getChildLayoutPosition(view)
                        val size = parent.adapter?.itemCount ?: 0
                        if (position in 0 .. size) {
                            when {
                                position % 2 == 0 -> {
                                    outRect.left = 10.dp
                                    outRect.right = 5.dp
                                }
                                position % 2 == 1 -> {
                                    outRect.left = 5.dp
                                    outRect.right = 10.dp
                                }
                            }
                            outRect.top = 10.dp
                        }
                    }
                })
            }
        }
    }

    override fun initFlow() {
//        lifecycleScope.launch {
//            viewModel.pager.collectLatest { pagingData ->
//                pokemonAdapter.submitData(pagingData)
//                binding.pbLoading.isVisible = false
//                binding.tvEmpty.isVisible = false
//            }
//        }
//        viewModel.goToDetail.observe(viewLifecycleOwner) { (type, pokemon) ->
//            findNavController().navigate(WishPokemonListDirections.actionWishToDetail(WishPokemon(name = pokemon.name, url = pokemon.url), type))
//        }
    }
}