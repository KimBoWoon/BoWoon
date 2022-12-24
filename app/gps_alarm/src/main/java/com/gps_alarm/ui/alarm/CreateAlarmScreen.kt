package com.gps_alarm.ui.alarm

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun CreateAlarmScreen(onNavigate: NavHostController) {
    CreateAlarmCompose()
}

@Composable
fun CreateAlarmCompose() {
    Text(text = "Create Alarm Screen")
}