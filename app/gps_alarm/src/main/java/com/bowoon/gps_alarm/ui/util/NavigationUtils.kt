package com.bowoon.gps_alarm.ui.util

import androidx.navigation.NavOptions
import com.bowoon.gpsAlarm.R

fun NavOptions.Builder.setFadeAnimation(): NavOptions.Builder {
    setEnterAnim(R.anim.fade_in)
        .setExitAnim(R.anim.fade_out)
        .setPopEnterAnim(R.anim.fade_in)
        .setPopExitAnim(R.anim.fade_out)

    return this
}