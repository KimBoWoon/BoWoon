package com.bowoon.gps_alarm.ui.fragments.vm

import com.bowoon.gpsAlarm.R
import com.bowoon.gps_alarm.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GpsAlarmVM @Inject constructor(
) : BaseVM() {
    enum class Navigation(title: String, tag: String) {
        ALARM("알람", R.id.nav_alarm.toString()),
        MAPS("지도", R.id.nav_maps.toString()),
        SETTING("설정", R.id.nav_setting.toString())
    }
}