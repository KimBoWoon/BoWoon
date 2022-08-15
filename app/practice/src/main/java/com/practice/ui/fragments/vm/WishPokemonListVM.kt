package com.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.practice.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WishPokemonListVM @Inject constructor(
//    private val roomHelper: RoomHelper
) : BaseVM() {
//    val goToDetail = MutableStateFlow<Pair<Int, WishPokemon?>>(Pair(0, null))
//    val pager = Pager(config = PagingConfig(pageSize = 20)) {
//        roomHelper.roomPokemonDao().getWishPokemon()
//    }.flow.cachedIn(viewModelScope)
}