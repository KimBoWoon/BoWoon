package com.bowoon.gps_alarm.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class SettingInfo(
    val isFollowing: Boolean? = null,
    val circleSize: Int? = null
) : Parcelable