package com.gps_alarm.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun dpToSp(value: Dp) = with(LocalDensity.current) { value.toSp() }