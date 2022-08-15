package com.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.domain.practice.dto.PokemonModel
import com.domain.practice.usecase.RoomUseCase
import com.practice.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailVM @Inject constructor(
    private val roomUseCase: RoomUseCase
) : BaseVM() {
    val addWish = MutableStateFlow<PokemonModel.Pokemon?>(null)
    val removeWish = MutableStateFlow<PokemonModel.Pokemon?>(null)

    suspend fun addWish(pokemon: PokemonModel.Pokemon): String =
        viewModelScope.async {
            roomUseCase.findPokemon(pokemon.name ?: "")?.let {
                "이미 등록됐습니다."
            } ?: run {
                roomUseCase.insert(pokemon)
                "등록됐습니다."
            }
        }.await()
}