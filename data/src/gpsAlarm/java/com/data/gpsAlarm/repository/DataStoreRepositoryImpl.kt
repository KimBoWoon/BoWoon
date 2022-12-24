package com.data.gpsAlarm.repository

import androidx.datastore.preferences.core.Preferences
import com.data.gpsAlarm.local.LocalDatastore
import com.domain.gpsAlarm.repository.DataStoreRepository

class DataStoreRepositoryImpl(
    private val dataStore: LocalDatastore
) : DataStoreRepository {
    override suspend fun <T> setDatastore(key: Preferences.Key<T>, value: T) {
        dataStore.set(key, value)
    }

    override suspend fun <T> getDatastore(key: Preferences.Key<T>): T? = dataStore.get(key)
}