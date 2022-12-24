package com.gps_alarm.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

//@Preview(
//    name = "preview light",
//    showBackground = true,
//    showSystemUi = false,
//    uiMode = Configuration.UI_MODE_NIGHT_NO
//)
//@Preview(
//    name = "preview dark",
//    showBackground = true,
//    showSystemUi = false,
//    uiMode = Configuration.UI_MODE_NIGHT_YES
//)
//@Composable
//fun PreviewWeatherItem() {
//    val weatherInfo = WeatherInfo()
//    val modifier = Modifier.padding()
//
//    WeatherItem(modifier, weatherInfo)
//}

@Composable
fun dpToSp(value: Dp) = with(LocalDensity.current) { value.toSp() }