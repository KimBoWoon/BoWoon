package com.domain.gpsAlarm.repository

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun <T> setDataStore(key: Preferences.Key<T>, value: T)
    suspend fun <T> getDataStore(key: Preferences.Key<T>): T?

    suspend fun <T> getData(key: Preferences.Key<T>): Flow<T?>
}