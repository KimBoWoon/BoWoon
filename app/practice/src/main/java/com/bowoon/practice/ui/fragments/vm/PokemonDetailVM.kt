package com.bowoon.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import com.bowoon.practice.base.BaseVM
import com.bowoon.practice.data.Pokemon
import com.bowoon.practice.room.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailVM @Inject constructor(
    private val roomRepository: RoomRepository
) : BaseVM() {
    val addWish = MutableStateFlow<Pokemon?>(null)
    val removeWish = MutableStateFlow<Pokemon?>(null)

    suspend fun addWish(pokemon: Pokemon): String =
        viewModelScope.async {
            roomRepository.findPokemon(pokemon.name ?: "")?.let {
                "이미 등록됐습니다."
            } ?: run {
                roomRepository.insert(pokemon)
                "등록됐습니다."
            }
        }.await()

    suspend fun removeWish(pokemon: Pokemon): String =
        viewModelScope.async {
            if (roomRepository.delete(pokemon) > 0) {
                "제거했습니다."
            } else {
                "데이터베이스에 저장되지 않았습니다."
            }
        }.await()
}