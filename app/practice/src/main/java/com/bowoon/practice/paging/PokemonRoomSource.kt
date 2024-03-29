package com.bowoon.practice.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bowoon.practice.data.Pokemon
import com.bowoon.practice.room.RoomRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RoomPokemonSource @Inject constructor(
    private val roomRepository: RoomRepository
) : PagingSource<Int, Pokemon>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        runCatching {
            roomRepository.getAllWishPokemon(params.loadSize)
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