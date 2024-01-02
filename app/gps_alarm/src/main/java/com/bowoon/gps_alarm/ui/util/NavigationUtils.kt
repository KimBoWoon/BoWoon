package com.bowoon.gps_alarm.ui.util

import androidx.navigation.NavOptions
import com.bowoon.gpsAlarm.R

fun NavOptions.Builder.setFadeAnimation(): NavOptions.Builder {
    setEnterAnim(com.bowoon.ui.R.anim.fade_in)
        .setExitAnim(com.bowoon.ui.R.anim.fade_out)
        .setPopEnterAnim(com.bowoon.ui.R.anim.fade_in)
        .setPopExitAnim(com.bowoon.ui.R.anim.fade_out)

    return this
}