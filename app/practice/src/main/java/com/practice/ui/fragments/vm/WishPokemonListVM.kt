package com.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.data.practice.paging.RoomPokemonSource
import com.domain.practice.usecase.RoomUseCase
import com.practice.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishPokemonListVM @Inject constructor(
    private val roomUseCase: RoomUseCase
) : BaseVM() {
    val pager = Pager(config = PagingConfig(pageSize = 20)) {
        RoomPokemonSource(roomUseCase)
    }.flow.cachedIn(viewModelScope)
}