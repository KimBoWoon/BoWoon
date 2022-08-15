package com.data.practice.repository

import androidx.datastore.preferences.core.Preferences
import com.data.practice.local.LocalDatastore
import com.domain.practice.repository.DataStoreRepository

class DataStoreRepositoryImpl(
    val datastore: LocalDatastore,
) : DataStoreRepository {
    override suspend fun <T> setDatastore(key: Preferences.Key<T>, value: T) {
        datastore.set(key, value)
    }

    override suspend fun <T> getDatastore(key: Preferences.Key<T>): T? = datastore.get(key)
}