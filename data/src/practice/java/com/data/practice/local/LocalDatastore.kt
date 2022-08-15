package com.data.practice.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.datastore by preferencesDataStore(name = "lol.datastore")

class LocalDatastore @Inject constructor(
    private val context: Context
) {
    object Keys {
        val LOL_VERSION = stringPreferencesKey("LOL_VERSION")
    }

    suspend fun <T> set(key: Preferences.Key<T>, value: T) {
        context.datastore.edit {
            it[key] = value
        }
    }

    suspend fun <T> get(key: Preferences.Key<T>): T? = context.datastore.data.map {
        it[key]
    }.firstOrNull()
}