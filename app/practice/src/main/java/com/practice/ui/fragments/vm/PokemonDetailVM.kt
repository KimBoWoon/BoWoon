package com.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.domain.practice.dto.SealedPokemon
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
    val addWish = MutableStateFlow<SealedPokemon.Pokemon?>(null)
    val removeWish = MutableStateFlow<SealedPokemon.Pokemon?>(null)

    suspend fun addWish(pokemon: SealedPokemon.Pokemon): String =
        viewModelScope.async {
            roomUseCase.findPokemon(pokemon.name ?: "")?.let {
                "이미 등록됐습니다."
            } ?: run {
                roomUseCase.insert(pokemon)
                "등록됐습니다."
            }
        }.await()

    suspend fun removeWish(pokemon: SealedPokemon.Pokemon): String =
        viewModelScope.async {
            if (roomUseCase.delete(pokemon) > 0) {
                "제거했습니다."
            } else {
                "데이터베이스에 저장되지 않았습니다."
            }
        }.await()
}