package com.practice.ui.fragments

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.domain.practice.dto.PokemonModel
import com.practice.R
import com.practice.base.BaseFragment
import com.practice.databinding.FragmentPokemonListBinding
import com.practice.paging.adapters.PokemonLoadPagingAdapter
import com.practice.paging.adapters.PokemonPagingAdapter
import com.practice.ui.dialog.PokemonDialog
import com.practice.ui.fragments.vm.PokemonListVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import util.ScreenUtils.dp

@AndroidEntryPoint
class PokemonListFragment : BaseFragment<FragmentPokemonListBinding>(
    R.layout.fragment_pokemon_list,
) {
    private val pokemonAdapter by lazy {
        PokemonPagingAdapter(ClickHandler()).apply {
            addLoadStateListener {
                if (it.source.refresh is LoadState.Error) {
                    binding?.rvPokemonList?.isVisible = false
                    binding?.tvEmpty?.text = (it.source.refresh as? LoadState.Error)?.error?.message
                    binding?.tvEmpty?.isVisible = true
                    binding?.pbLoading?.isVisible = false
                    PokemonDialog(
                        "네트워크에 문제가 있는거 같습니다.\n다시 연결하시겠습니까?",
                        "예",
                        { retry() },
                        "아니오",
                        {}
                    ).show(childFragmentManager, PokemonListFragment::class.java.simpleName)
                } else if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && (binding?.rvPokemonList?.adapter?.itemCount ?: 0) < 1) {
                    binding?.rvPokemonList?.isVisible = false
                    binding?.tvEmpty?.isVisible = true
                    binding?.pbLoading?.isVisible = false
                } else {
                    binding?.rvPokemonList?.isVisible = true
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
    private val viewModel by viewModels<PokemonListVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            lifecycleOwner = this@PokemonListFragment
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding?.apply {
            rvPokemonList.apply {
                adapter = pokemonAdapter.withLoadStateHeaderAndFooter(
                    PokemonLoadPagingAdapter { pokemonAdapter.retry() },
                    PokemonLoadPagingAdapter { pokemonAdapter.retry() }
                )
                layoutManager = GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false).apply {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int =
                            if (pokemonAdapter.getItemViewType(position) == R.layout.viewholder_pokemon) {
                                1
                            } else {
                                2
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
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.pokemonPageFlow.collectLatest { pagingData ->
                pokemonAdapter.submitData(pagingData)
                binding?.pbLoading?.isVisible = false
                binding?.tvEmpty?.isVisible = false
            }
        }
    }

    inner class ClickHandler {
        fun goToDetail(pokemon: PokemonModel.Pokemon, type: Int) {
            findNavController().navigate(R.id.action_home_to_detail, Bundle().apply {
                putParcelable("pokemon", pokemon)
                putInt("type", type)
            })
        }
    }
}