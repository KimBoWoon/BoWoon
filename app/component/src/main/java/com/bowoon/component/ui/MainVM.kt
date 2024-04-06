package com.bowoon.component.ui

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bowoon.component.source.PagingSource
import com.bowoon.component.apis.Apis
import com.bowoon.component.data.Pokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val apis: Apis
) : ViewModel(), LifecycleObserver {
    val pokemonPageFlow: Flow<PagingData<Pokemon>> = Pager(
        PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5)
    ) {
        PagingSource(apis)
    }.flow
}