package com.domain.practice.usecase

import androidx.datastore.preferences.core.Preferences
import com.domain.practice.repository.DataStoreRepository

class DataStoreUseCase(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        dataStoreRepository.setDatastore(key, value)
    }

    suspend fun <T> get(key: Preferences.Key<T>): T? =
        dataStoreRepository.getDatastore(key)
}