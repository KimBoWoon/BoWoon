package com.bowoon.practice.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bowoon.practice.R
import com.bowoon.practice.base.BaseFragment
import com.bowoon.practice.databinding.FragmentPokemonDetailBinding
import com.bowoon.practice.ui.fragments.vm.PokemonDetailVM
import com.bowoon.commonutils.ContextUtils.showToast
import com.bowoon.practice.data.Pokemon
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random

@AndroidEntryPoint
class PokemonDetailFragment : BaseFragment<FragmentPokemonDetailBinding>(
    R.layout.fragment_pokemon_detail,
) {
//    private val pokemon by navArgs<PokemonDetailFragmentArgs>()
    private val viewModel by viewModels<PokemonDetailVM>()
    private var pokemon: Pokemon? = null
    private var type: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = this@PokemonDetailFragment
            vm = viewModel
        }
        lifecycle.addObserver(viewModel)

        requireArguments().apply {
            pokemon = getParcelable("pokemon")
            type = getInt("type")
        }

        initBinding()
        initFlow()
    }

    override fun initBinding() {
        binding.apply {
            pokemon?.let {
                when (type) {
                    0 -> {
                        bRegisterWish.text = "위시 등록"
                        bRegisterWish.setOnClickListener {
                            viewModel.addWish.value = pokemon
                        }
                    }
                    1 -> {
                        bRegisterWish.text = "위시 제거"
                        bRegisterWish.setOnClickListener {
                            viewModel.removeWish.value = pokemon
                        }
                    }
                    else -> {}
                }
            }
            pokemon?.let { pokemon ->
                dto = pokemon
                pbHpProgress.progress = Random.nextInt(300)
                pbAtkProgress.progress = Random.nextInt(300)
                pbDefProgress.progress = Random.nextInt(300)
                pbSpdProgress.progress = Random.nextInt(300)
            }
        }
    }

    override fun initFlow() {
        lifecycleScope.launch {
            viewModel.addWish.collect { pokemon ->
                pokemon?.let {
                    viewModel.addWish(pokemon).apply {
                        requireContext().showToast(this, Toast.LENGTH_SHORT)
                    }
                    findNavController().popBackStack()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.removeWish.collect { pokemon ->
                pokemon?.let {
                    viewModel.removeWish(pokemon).apply {
                        requireContext().showToast(this, Toast.LENGTH_SHORT)
                    }
                    findNavController().popBackStack()
                }
            }
        }
    }
}