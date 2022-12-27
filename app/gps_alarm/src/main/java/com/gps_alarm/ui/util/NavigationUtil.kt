package com.gps_alarm.ui.util

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

val alarmDeepLink = listOf(
    navDeepLink {
        uriPattern = "gps_alarm://alarm_detail/{addressId}"
    }
)
val alarmDetailArgument = listOf(
    navArgument("addressId") {
        type = NavType.IntType
        defaultValue = -1
        nullable = false
    }
)