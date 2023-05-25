package com.domain.gpsAlarm.repository

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepository {
    suspend fun <T> setDataStore(key: Preferences.Key<T>, value: T)
    suspend fun <T> getDataStore(key: Preferences.Key<T>): T?
}