package com.bowoon.component.ui

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bowoon.commonutils.DataStatus
import com.bowoon.commonutils.replaceUriParameter
import com.bowoon.component.apis.Apis
import com.bowoon.component.data.ComponentData
import com.bowoon.component.data.Components
import com.bowoon.component.data.Pokemon
import com.bowoon.component.data.PokemonData
import com.bowoon.component.source.PagingSource
import com.bowoon.component.utils.ComponentUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val apis: Apis,
    private val json: Json,
    private val componentUtils: ComponentUtils
) : ViewModel(), LifecycleObserver {
    companion object {
        private const val TAG = "component_main_vm"
    }

    val componentData = MutableStateFlow<DataStatus<List<Components>?>>(DataStatus.Loading)
    val pokemonPageFlow: Flow<PagingData<Pokemon>> by lazy {
        Pager(
            PagingConfig(pageSize = 20, initialLoadSize = 20, prefetchDistance = 5)
        ) {
            PagingSource(apis, pagingUrl)
        }.flow.cachedIn(viewModelScope)
    }
    private var pagingUrl = ""
    private var count = -20

    suspend fun getData(): PokemonData {
        count += 20
        return apis.pagingService.getListData(
            replaceUriParameter(
                Uri.parse(pagingUrl),
                listOf(Pair("limit", "20"), Pair("offset", "$count"))
            ).toString()
        )
    }

    fun fetchComponent(context: Context) {
        componentData.value = DataStatus.Loading
        context.assets.open("component.json").use { inputStream ->
            runCatching {
                json.decodeFromString<ComponentData>(String(inputStream.readBytes(), Charsets.UTF_8))
            }.onSuccess {
                (DataStatus.Success(
                    it.components?.filterNotNull()
                        ?.map { component -> componentUtils.createComponent(component) }).data?.filterIsInstance<Components.ListComponent>()
                    ?.firstOrNull()?.url ?: "").also { pagingUrl ->
                        if (pagingUrl.isNotEmpty()) {
                            this@MainVM.pagingUrl = pagingUrl
                        }
                }
                componentData.value = DataStatus.Success(it.components?.filterNotNull()?.map { component -> componentUtils.createComponent(component) })
            }.onFailure { e ->
                componentData.value = DataStatus.Failure(e)
            }
        }
    }
}