//package com.data.lol.repository
//
//import androidx.datastore.preferences.core.Preferences
//import com.domain.lol.repository.DatastoreRepository
//
//class DatastoreRepositoryImpl(
//    private val datastore: LocalDatastore,
//) : DatastoreRepository {
//    override suspend fun <T> setDatastore(key: Preferences.Key<T>, value: T) {
//        datastore.set(key, value)
//    }
//
//    override suspend fun <T> getDatastore(key: Preferences.Key<T>): T? = datastore.get(key)
//}