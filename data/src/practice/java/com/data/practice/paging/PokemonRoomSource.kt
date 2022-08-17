package com.data.practice.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.domain.practice.dto.PokemonModel
import com.domain.practice.usecase.RoomUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoomPokemonSource @Inject constructor(
    private val roomUseCase: RoomUseCase
) : PagingSource<Int, PokemonModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
        runCatching {
            roomUseCase.getAllWishPokemon(params.loadSize)
        }.onSuccess{ response ->
            return LoadResult.Page(
                data = response.first(),
                prevKey = null, // Only paging forward.
                nextKey = if (params.loadSize == response.first().size) (params.key ?: 0) else null
            )
        }.onFailure { e ->
            e.printStackTrace()
            return LoadResult.Error(e)
        }

        return LoadResult.Error(Throwable("Paging error"))
    }

    override fun getRefreshKey(state: PagingState<Int, PokemonModel>): Int? {
        // prevKet == null -> 첫 번째 페이지
        // nextKey == null -> 마지막 페이지
        // prevKey == null && nextKey == null -> 최초 페이지
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}