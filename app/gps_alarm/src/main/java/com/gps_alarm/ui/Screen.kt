package com.gps_alarm.ui

import androidx.annotation.StringRes
import com.bowoon.android.gps_alarm.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Alarm : Screen("alarm", R.string.bottom_alarm)
    object Maps : Screen("maps", R.string.bottom_maps)
    object Setting : Screen("setting", R.string.bottom_setting)
    object CreateAlarm : Screen("createAlarm", R.string.create_alarm)
}