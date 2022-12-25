package com.gps_alarm.paging.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class Address(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val distance: Double? = null,
    val englishAddress: String? = null,
    val jibunAddress: String? = null,
    val roadAddress: String? = null,
    val x: Double? = null,
    val y: Double? = null
)