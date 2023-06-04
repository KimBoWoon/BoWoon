package com.gps_alarm.ui.theme

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.gps_alarm.data.GpsAlarmConstant

fun Modifier.getSixteenByNineSize(): Modifier = composed {
    val density = LocalConfiguration.current

    this
        .fillMaxWidth()
        .height((density.screenWidthDp.toFloat() * GpsAlarmConstant.SIXTEEN_BY_NINE_RATE).dp)
}