package com.data.practice.paging

import android.content.res.Resources
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.data.practice.dto.Pokemon
import com.data.practice.room.RoomDataBase
import com.domain.practice.usecase.PokemonApiUseCase
import javax.inject.Inject

/**
 * 대량의 데이터를 받아오고 데이터베이스에 저장한 뒤에 필요한 시점에 다시 네트워크 요청
 */
@OptIn(ExperimentalPagingApi::class) // 안정화 되지 않은 라이브러리의 사용을 명시
class PokemonRemoteMediator @Inject constructor(
    private val roomDataBase: RoomDataBase,
    private val pokemonApiUseCase: PokemonApiUseCase
) : RemoteMediator<Int, Pokemon>() {
    private val limit = 100
    private var offset = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Pokemon>
    ): MediatorResult {
        return try {
            when (loadType) {
                LoadType.REFRESH -> {
                    refresh()
                }
                LoadType.PREPEND -> {
//                    loadBefore()
                    // 로드가 성공했고 받은 아이템 목록이 비어 있다면 true를 반환
                    MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    loadAfter()
                }
            }
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

        // 만약 네트워크 요청으로 인해 에러가 발생한다면 MediatorResult.Error을 반환
        return MediatorResult.Error(Resources.NotFoundException())
    }

    /**
     * 새로고침
     */
    private suspend fun refresh(): MediatorResult {
        return try {
            val response = pokemonApiUseCase.getPokemon(limit, offset)
            if (response.results?.isNotEmpty() == true) {
                roomDataBase.transactionExecutor.execute {
//                    roomDataBase.roomPokemonDao().deleteAll()
//                    roomDataBase.roomPokemonDao().insertAll(response.results.map {
//                        Pokemon(name = it.name ?: "", url = it.url ?: "")
//                    })
                }
            }
            offset = 100
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    /**
     * 이전 아이템
     */
    private suspend fun loadBefore(): MediatorResult {
        offset -= 100 // 이전 아이템의 offset 설정
        return try {
            val response = pokemonApiUseCase.getPokemon(limit, offset)
            if (response.results?.isNotEmpty() == true) {
                roomDataBase.transactionExecutor.execute {
//                    roomDataBase.roomPokemonDao().insertAll(response.results.map {
//                        Pokemon(name = it.name ?: "", url = it.url ?: "")
//                    })
                }
            }
            // 로드가 성공했고 받은 아이템 목록이 비어있지 않다면, 아이템 목록을 데이터베이스에 저장하고 false를 반환
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    /**
     * 다음 아이템
     */
    private suspend fun loadAfter(): MediatorResult {
        return try {
            val response = pokemonApiUseCase.getPokemon(limit, offset)
            if (response.results?.isNotEmpty() == true) {
                roomDataBase.transactionExecutor.execute {
//                    roomDataBase.roomPokemonDao().insertAll(response.results.map {
//                        Pokemon(name = it.name ?: "", url = it.url ?: "")
//                    })
                }
            }
            offset += 100 // 다음 받아올 아이템의 offset 설정
            // 로드가 성공했고 받은 아이템 목록이 비어있지 않다면, 아이템 목록을 데이터베이스에 저장하고 false를 반환
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}