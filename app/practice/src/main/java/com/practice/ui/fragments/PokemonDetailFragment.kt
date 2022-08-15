package com.practice.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.domain.practice.dto.PokemonModel
import com.practice.R
import com.practice.base.BaseFragment
import com.practice.databinding.FragmentPokemonDetailBinding
import com.practice.ui.fragments.vm.PokemonDetailVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

@AndroidEntryPoint
class PokemonDetailFragment : BaseFragment<FragmentPokemonDetailBinding>(
    R.layout.fragment_pokemon_detail,
) {
    //    @Inject
//    lateinit var roomHelper: RoomHelper
//    private val pokemon by navArgs<PokemonDetailFragmentArgs>()
    private val viewModel by viewModels<PokemonDetailVM>()
    private var pokemon: PokemonModel.Pokemon? = null
    private var type: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
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
        binding?.apply {
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
                    val toastMessage = viewModel.addWish(pokemon)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
            }
        }

//        viewModel.removeWish.observe(viewLifecycleOwner) { pokemon ->
//            lifecycleScope.launch(Dispatchers.IO) {
//                val toastMessage = roomHelper.roomPokemonDao().findPokemon(pokemon.name)?.let {
//                    roomHelper.roomPokemonDao().delete(it)
//                    "성공적 으로 제거 했습니다."
//                } ?: kotlin.run {
//                    "알 수 없는 오류로 인해 제거하지 못했습니다."
//                }
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
//                    findNavController().popBackStack()
//                }
//            }
//        }
    }
}