package com.gps_alarm.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
@Parcelize
data class Address(
    val name: String? = null,
    var isEnable: Boolean? = null,
    val distance: Double? = null,
    val englishAddress: String? = null,
    val jibunAddress: String? = null,
    val roadAddress: String? = null,
    @SerialName("x")
    val longitude: Double? = null,
    @SerialName("y")
    val latitude: Double? = null
) : Parcelable