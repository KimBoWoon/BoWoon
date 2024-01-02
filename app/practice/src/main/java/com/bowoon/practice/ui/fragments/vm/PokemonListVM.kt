package com.bowoon.practice.ui.fragments.vm

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bowoon.practice.apis.Apis
import com.bowoon.practice.base.BaseVM
import com.bowoon.practice.data.Pokemon
import com.bowoon.practice.paging.PokemonApiSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonListVM @Inject constructor(
    private val api: Apis
) : BaseVM() {
    val pokemonPageFlow: Flow<PagingData<Pokemon>> = Pager(
        PagingConfig(pageSize = 20, initialLoadSize = 20)
    ) {
        PokemonApiSource(api) // TODO 유즈 케이스를 di 사용해야함
    }.flow.cachedIn(viewModelScope)
}