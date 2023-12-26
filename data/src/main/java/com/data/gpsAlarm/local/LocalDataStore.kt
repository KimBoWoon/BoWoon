package com.data.gpsAlarm.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.datastore by preferencesDataStore(name = "gpsAlarm.datastore")

class LocalDataStore @Inject constructor(
    private val context: Context
) {
    object Keys {
        val alarmList = stringSetPreferencesKey("ALARM_LIST")
        val setting = stringPreferencesKey("SETTING")
    }

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        context.datastore.edit {
            it[key] = value
        }
    }

    suspend fun <T> get(key: Preferences.Key<T>): T? = context.datastore.data.map {
        it[key]
    }.firstOrNull()

    suspend fun <T> getFlow(key: Preferences.Key<T>): Flow<T?> = flow { emit(context.datastore.data.map { it[key] }.firstOrNull()) }
}