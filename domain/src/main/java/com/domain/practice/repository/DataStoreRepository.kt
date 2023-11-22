package com.domain.practice.repository

import androidx.datastore.preferences.core.Preferences

interface DataStoreRepository {
    suspend fun <T> setDatastore(key: Preferences.Key<T>, value: T)
    suspend fun <T> getDatastore(key: Preferences.Key<T>): T?
}