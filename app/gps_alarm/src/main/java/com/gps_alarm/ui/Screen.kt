package com.gps_alarm.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.bowoon.android.gps_alarm.R

sealed class Screen(
    val route: String,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    object Alarm : Screen("alarm", Icons.Filled.List, R.string.bottom_alarm)
    object Maps : Screen("maps", Icons.Filled.LocationOn, R.string.bottom_maps)
    object Setting : Screen("setting", Icons.Filled.Settings, R.string.bottom_setting)
    object CreateAlarm : Screen("create_alarm", Icons.Filled.Favorite, R.string.create_alarm)
    object AlarmDetail : Screen("alarm_detail", Icons.Filled.Favorite, R.string.alarm_detail)
}