package com.data.gpsAlarm.repository

import androidx.datastore.preferences.core.Preferences
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.gpsAlarm.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class DataStoreRepositoryImpl(
    private val dataStore: LocalDataStore
) : DataStoreRepository {
    override suspend fun <T> setDataStore(key: Preferences.Key<T>, value: T) {
        dataStore.set(key, value)
    }

    override suspend fun <T> getDataStore(key: Preferences.Key<T>): T? = dataStore.get(key)

    override suspend fun <T> getData(key: Preferences.Key<T>): Flow<T?> = dataStore.getFlow(key)
}