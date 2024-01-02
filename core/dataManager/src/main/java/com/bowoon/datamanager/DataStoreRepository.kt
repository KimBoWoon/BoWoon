package com.bowoon.datamanager

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val GPS_ALARM_DATA_STORE_NAME = "gpsAlarm"
    }

    private val dataStoreMap = mutableMapOf<String, DataStore<Preferences>>()

    private fun createDataStore(name: String): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler { emptyPreferences() },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { context.preferencesDataStoreFile(name) }
        ).run {
            dataStoreMap[name] = this
            this
        }

    private fun getDataStore(name: String): DataStore<Preferences> =
        dataStoreMap[name] ?: createDataStore(name)

    suspend fun <T> setData(
        name: String,
        key: Preferences.Key<T>,
        value: T
    ) {
        getDataStore(name).edit {
            it[key] = value
        }
    }

    suspend fun <T> setData(name: String, key: String, value: T) {
        getDataStore(name).edit {
            when (value) {
                is Int -> it[intPreferencesKey(key)] = value
                is Long -> it[longPreferencesKey(key)] = value
                is Float -> it[floatPreferencesKey(key)] = value
                is Double -> it[doublePreferencesKey(key)] = value
                is String -> it[stringPreferencesKey(key)] = value
                is Boolean -> it[booleanPreferencesKey(key)] = value
                else -> it[stringPreferencesKey(key)] = value.toString()
            }
        }
    }

    suspend fun <T> getData(
        name: String,
        key: Preferences.Key<T>,
        defValue: T?
    ): T? = getDataStore(name).data.map { preferences ->
        preferences[key] ?: defValue
    }.firstOrNull()

    suspend fun getData(
        name: String,
        key: String,
        defValue: Any?
    ): Any? = getDataStore(name).data.map { preferences ->
        preferences.asMap().keys.firstOrNull { preferenceKey ->
            preferenceKey.name == key
        }?.let { preferenceKey ->
            preferences[preferenceKey]
        } ?: defValue
    }.firstOrNull()

    suspend fun <T> getDataFlow(
        name: String,
        key: Preferences.Key<T>,
        defValue: T?
    ): Flow<T?> = flow {
        emit(getData(name, key, defValue) ?: defValue)
    }

    suspend fun getDataFlow(
        name: String,
        key: String,
        defValue: Any?
    ): Flow<Any?> = flow {
        emit(getData(name, key, defValue) ?: defValue)
    }

    suspend fun <T> remove(name: String, key: Preferences.Key<T>) {
        getDataStore(name).edit {
            it.remove(key)
        }
    }

    suspend fun remove(name: String, key: String) {
        getDataStore(name).edit {
            it.asMap().keys.firstOrNull { preferenceKey ->
                preferenceKey.name == key
            }?.let { preferenceKey ->
                it.remove(preferenceKey)
            } ?: it.remove(stringPreferencesKey(key))
        }
    }

    suspend fun clear(name: String) {
        getDataStore(name).edit {
            it.clear()
        }
    }
}