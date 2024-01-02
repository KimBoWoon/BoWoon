package com.bowoon.gps_alarm.ui.viewmodel

import com.bowoon.gps_alarm.base.BaseVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingVM @Inject constructor(
) : BaseVM() {
    enum class Setting {
        IS_FOLLOW,
        CIRCLE_SIZE
    }
}