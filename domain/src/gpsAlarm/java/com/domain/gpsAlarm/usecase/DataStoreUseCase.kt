package com.domain.gpsAlarm.usecase

import androidx.datastore.preferences.core.Preferences
import com.domain.gpsAlarm.repository.DataStoreRepository

class DataStoreUseCase(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStoreRepository.setDataStore(key, value)
    }

    suspend fun <T> get(key: Preferences.Key<T>): T? =
        dataStoreRepository.getDataStore(key)
}