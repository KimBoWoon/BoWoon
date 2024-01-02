package com.bowoon.datamanager

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private fun getDataStore(name: String): SharedPreferences? =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    fun <T> setData(name: String, key: String, value: T) {
        when (value) {
            is Int -> context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, value)
            is Long -> context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putLong(key, value)
            is Float -> context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putFloat(key, value)
            is String -> context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value)
            is Boolean -> context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putBoolean(key, value)
            else -> context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putString(key, value.toString())
        }.apply()
    }

    suspend fun <T> getData(
        name: String,
        key: String,
        defValue: T?
    ): T? = when (defValue) {
        is Int -> context.getSharedPreferences(name, Context.MODE_PRIVATE).getInt(key, defValue) as T
        is Long -> context.getSharedPreferences(name, Context.MODE_PRIVATE).getLong(key, defValue) as T
        is Float -> context.getSharedPreferences(name, Context.MODE_PRIVATE).getFloat(key, defValue) as T
        is String -> context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defValue) as T
        is Boolean -> context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, defValue) as T
        else -> context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defValue.toString()) as T
    }

    suspend fun getDataFlow(
        name: String,
        key: String,
        defValue: Any?
    ): Flow<Any?> = flow {
        emit(getData(name, key, defValue) ?: defValue)
    }

    fun remove(name: String, key: String) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().remove(key).apply()
    }

    fun clear(name: String) {
        context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().clear().apply()
    }
}