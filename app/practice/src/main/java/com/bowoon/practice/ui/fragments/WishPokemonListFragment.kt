package com.bowoon.practice.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bowoon.practice.R
import com.bowoon.practice.base.BaseFragment
import com.bowoon.practice.databinding.FragmentWishPokemonBinding
import com.bowoon.practice.ui.activities.vm.MainVM
import com.bowoon.practice.ui.adapters.PokemonLoadPagingAdapter
import com.bowoon.practice.ui.adapters.WishPokemonPagingAdapter
import com.bowoon.practice.ui.fragments.vm.WishPokemonListVM
import com.bowoon.practice.data.Pokemon
import com.bowoon.practice.util.PokemonDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishPokemonListFragment : BaseFragment<FragmentWishPokemonBinding>(
    R.layout.fragment_wish_pokemon,
) {
    private val pokemonAdapter by lazy {
        WishPokemonPagingAdapter(ClickHandler()).apply {
            addLoadStateListener {
                if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && (binding.rvWishPokemonList.adapter?.itemCount ?: 0) < 1) {
                    binding.rvWishPokemonList.isVisible = false
                    binding.tvEmpty.isVisible = true
                    binding.pbLoading.isVisible = false
                } else {
                    binding.rvWishPokemonList.isVisible = true
                    binding.tvEmpty.isVisible = false
                    binding.pbLoading.isVisible = false
                }
                when (it.refresh) {
                    is LoadState.Loading -> {
                        binding.pbLoading.isVisible = true
                    }
                    is LoadState.Error -> {
                        binding.pbLoading.isVisible = false
                    }
                    is LoadState.NotLoading -> {
                        binding.pbLoading.isVisible = false
                    }
                }
            }
        }
    }
    private val viewModel by viewModels<WishPokemonListVM>()
    private val activityVM by activityViewModels<MainVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@WishPokemonListFragment
        }
        lifecycle.addObserver(viewModel)

        initBinding()
        initFlow()
    }

    override fun onResume() {
        super.onResume()
        pokemonAdapter.refresh()
    }

    override fun initBinding() {
        binding.rvWishPokemonList.apply {
            adapter = pokemonAdapter.withLoadStateFooter(
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
                addItemDecoration(PokemonDecorator())
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.pager.collectLatest { pagingData ->
                pokemonAdapter.submitData(pagingData)
                binding.pbLoading.isVisible = false
                binding.tvEmpty.isVisible = false
            }
        }
    }

    inner class ClickHandler {
        fun goToDetail(type: Int, pokemon: Pokemon) {
            findNavController().navigate(R.id.action_wish_to_detail, Bundle().apply {
                putParcelable("pokemon", Pokemon(pokemon.name, pokemon.url))
                putInt("type", type)
            })
        }
    }
}