package com.gps_alarm.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.bowoon.android.gps_alarm.R

enum class NavigationScreen(
    val route: String,
    val icon: ImageVector,
    @StringRes val resourceId: Int
) {
    Alarm("alarm", Icons.Filled.List, R.string.bottom_alarm),
    Maps("maps", Icons.Filled.LocationOn, R.string.bottom_maps),
    Setting("setting", Icons.Filled.Settings, R.string.bottom_setting),
    CreateAlarm("create_alarm", Icons.Filled.Favorite, R.string.create_alarm),
    AlarmDetail("alarm_detail", Icons.Filled.Favorite, R.string.alarm_detail);
}