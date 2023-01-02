package com.gps_alarm.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.gps_alarm.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GpsAlarmVM @Inject constructor(

) : BaseVM() {
    var appBarTitle = mutableStateOf("GPS 알람")
}