package com.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.data.practice.paging.source.RoomPokemonSource
import com.domain.practice.usecase.RoomUseCase
import com.practice.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WishPokemonListVM @Inject constructor(
    private val roomUseCase: RoomUseCase
) : BaseVM() {
    //    val goToDetail = MutableStateFlow<Pair<Int, WishPokemon?>>(Pair(0, null))
    val pager = Pager(config = PagingConfig(pageSize = 20)) {
        RoomPokemonSource(roomUseCase)
    }.flow.cachedIn(viewModelScope)
}