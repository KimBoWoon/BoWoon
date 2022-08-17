package com.data.practice.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.domain.practice.dto.PokemonModel
import com.domain.practice.usecase.PokemonApiUseCase
import javax.inject.Inject

class PokemonApiSource @Inject constructor(
    private val pokemonApiUseCase: PokemonApiUseCase
) : PagingSource<Int, PokemonModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
        runCatching {
            pokemonApiUseCase.getPokemon(params.loadSize, params.key ?: 0)
        }.onSuccess{ response ->
            return LoadResult.Page(
                data = response.results ?: listOf(),
                prevKey = null, // Only paging forward.
                nextKey = params.loadSize + (params.key ?: 0)
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