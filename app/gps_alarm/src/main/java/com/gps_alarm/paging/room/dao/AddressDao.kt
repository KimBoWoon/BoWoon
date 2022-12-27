package com.gps_alarm.paging.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gps_alarm.paging.room.entity.Address

@Dao
interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<Address>)

    @Query("SELECT * FROM address")
    fun getAll(): List<Address>

    @Query("SELECT * FROM address")
    fun pagingSource(): PagingSource<Int, Address>

    @Query("DELETE FROM address")
    suspend fun clearAll()

    @Query("SELECT * FROM address WHERE id = :addressId")
    suspend fun getAddress(addressId: Int): Address
}