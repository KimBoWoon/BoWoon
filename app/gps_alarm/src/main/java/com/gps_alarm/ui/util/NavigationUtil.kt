package com.gps_alarm.ui.util

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

val alarmDeepLink = listOf(
    navDeepLink {
        uriPattern = "gps_alarm://alarm_detail/{longitude}/{latitude}"
    }
)
val alarmDetailArgument = listOf(
    navArgument("longitude") {
        type = NavType.StringType
        defaultValue = ""
        nullable = false
    },
    navArgument("latitude") {
        type = NavType.StringType
        defaultValue = ""
        nullable = false
    }
)