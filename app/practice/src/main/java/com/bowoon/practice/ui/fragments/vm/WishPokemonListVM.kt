package com.bowoon.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.bowoon.practice.base.BaseVM
import com.bowoon.practice.paging.RoomPokemonSource
import com.bowoon.practice.room.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishPokemonListVM @Inject constructor(
    private val roomRepository: RoomRepository
) : BaseVM() {
    val pager = Pager(config = PagingConfig(pageSize = 20)) {
        RoomPokemonSource(roomRepository)
    }.flow.cachedIn(viewModelScope)
}