package com.gps_alarm.paging.source

import androidx.paging.*
import androidx.room.*
import com.bumptech.glide.load.HttpException
import com.data.gpsAlarm.local.LocalDatastore
import com.gps_alarm.paging.room.AppDatabase
import com.gps_alarm.paging.room.entity.Address
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class GeocodeSource @Inject constructor(
    private val localDatastore: LocalDatastore,
    private val database: AppDatabase
) : RemoteMediator<Int, Address>() {
    private val addressDao = database.addressDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Address>
    ): MediatorResult {
        return try {
            val response = localDatastore.get(LocalDatastore.Keys.alarmList)?.map { address ->
                val decodeAddress = Json.decodeFromString<com.data.gpsAlarm.dto.Addresses>(address)
                Address(
                    distance = decodeAddress.distance,
                    englishAddress = decodeAddress.englishAddress,
                    jibunAddress = decodeAddress.jibunAddress,
                    roadAddress = decodeAddress.roadAddress,
                    x = decodeAddress.x,
                    y = decodeAddress.y
                )
            } ?: run {
                emptyList()
            }

            database.withTransaction {
                addressDao.clearAll()
                addressDao.insertAll(response)
            }

            MediatorResult.Success(endOfPaginationReached = true)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}