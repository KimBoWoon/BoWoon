package com.bowoon.component.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bowoon.commonutils.Log
import com.bowoon.component.apis.Apis
import com.bowoon.component.data.Pokemon

class PagingSource(
    private val apis: Apis
) : PagingSource<Int, Pokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        runCatching {
            apis.pagingService.getListData("https://pokeapi.co/api/v2/pokemon?limit=${params.loadSize}&offset=${params.key ?: 0}")
        }.onSuccess { response ->
            return LoadResult.Page(
                data = response.results ?: emptyList(),
                prevKey = null, // Only paging forward.
                nextKey = if (response.next != null) params.loadSize + (params.key ?: 0) else null
            )
        }.onFailure { e ->
            Log.printStackTrace(e)
            return LoadResult.Error(e)
        }

        return LoadResult.Error(Throwable("Paging error"))
    }

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        // prevKet == null -> 첫 번째 페이지
        // nextKey == null -> 마지막 페이지
        // prevKey == null && nextKey == null -> 최초 페이지
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}