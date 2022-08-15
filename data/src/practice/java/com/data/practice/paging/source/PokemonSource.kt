package com.data.practice.paging.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.domain.practice.dto.PokemonModel
import com.domain.practice.usecase.PokemonApiUseCase

class PokemonSource(
    private val pokemonApiUseCase: PokemonApiUseCase
//    private val pokemonApiService: PokemonApiService
) : PagingSource<Int, PokemonModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
//        delay(5000) for test
        runCatching {
            val response = pokemonApiUseCase.getAllPokemon(params.loadSize, params.key ?: 0)
            return LoadResult.Page(
                data = response.results?.toList() ?: listOf(),
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

/**
 * 대량의 데이터를 받아오고 데이터베이스에 저장한 뒤에 필요한 시점에 다시 네트워크 요청
 */
//@OptIn(ExperimentalPagingApi::class) // 안정화 되지 않은 라이브러리의 사용을 명시
//class PokemonRemoteMediator(
//    private val database: RoomHelper,
//    private val networkService: com.bowoon.android.jetpackpractice.api.Pokemon
//) : RemoteMediator<Int, Pokemon>() {
//    private val limit = 100
//    private var offset = 0
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, Pokemon>
//    ): MediatorResult {
//        return try {
//            when (loadType) {
//                LoadType.REFRESH -> {
//                    refresh()
//                }
//                LoadType.PREPEND -> {
////                    loadBefore()
//                    // 로드가 성공했고 받은 아이템 목록이 비어 있다면 true를 반환
//                    MediatorResult.Success(endOfPaginationReached = true)
//                }
//                LoadType.APPEND -> {
//                    loadAfter()
//                }
//            }
//        } catch (e: Exception) {
//            MediatorResult.Error(e)
//        }
//
//        // 만약 네트워크 요청으로 인해 에러가 발생한다면 MediatorResult.Error을 반환
////        return MediatorResult.Error(Resources.NotFoundException())
//    }
//
//    /**
//     * 새로고침
//     */
//    private suspend fun refresh(): MediatorResult {
//        return try {
//            val response = networkService.getPokemon(limit, offset)
//            if (response.results?.isNotEmpty() == true) {
//                database.transactionExecutor.execute {
//                    database.roomPokemonDao().deleteAll()
//                    database.roomPokemonDao().insertAll(response.results.map {
//                        RoomPokemon(name = it.name ?: "", url = it.url ?: "")
//                    })
//                }
//            }
//            offset = 100
//            MediatorResult.Success(endOfPaginationReached = false)
//        } catch (e: Exception) {
//            MediatorResult.Error(e)
//        }
//    }
//
//    /**
//     * 이전 아이템
//     */
//    private suspend fun loadBefore(): MediatorResult {
//        offset -= 100 // 이전 아이템의 offset 설정
//        return try {
//            val response = networkService.getPokemon(limit, offset)
//            if (response.results?.isNotEmpty() == true) {
//                database.transactionExecutor.execute {
//                    database.roomPokemonDao().insertAll(response.results.map {
//                        RoomPokemon(name = it.name ?: "", url = it.url ?: "")
//                    })
//                }
//            }
//            // 로드가 성공했고 받은 아이템 목록이 비어있지 않다면, 아이템 목록을 데이터베이스에 저장하고 false를 반환
//            MediatorResult.Success(endOfPaginationReached = false)
//        } catch (e: Exception) {
//            MediatorResult.Error(e)
//        }
//    }
//
//    /**
//     * 다음 아이템
//     */
//    private suspend fun loadAfter(): MediatorResult {
//        return try {
//            val response = networkService.getPokemon(limit, offset)
//            if (response.results?.isNotEmpty() == true) {
//                database.transactionExecutor.execute {
//                    database.roomPokemonDao().insertAll(response.results.map {
//                        RoomPokemon(name = it.name ?: "", url = it.url ?: "")
//                    })
//                }
//            }
//            offset += 100 // 다음 받아올 아이템의 offset 설정
//            // 로드가 성공했고 받은 아이템 목록이 비어있지 않다면, 아이템 목록을 데이터베이스에 저장하고 false를 반환
//            MediatorResult.Success(endOfPaginationReached = false)
//        } catch (e: Exception) {
//            MediatorResult.Error(e)
//        }
//    }
//}