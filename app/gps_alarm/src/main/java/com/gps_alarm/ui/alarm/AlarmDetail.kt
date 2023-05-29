package com.gps_alarm.ui.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gps_alarm.data.Address
import com.gps_alarm.ui.util.FixedMarkerMap
import com.gps_alarm.ui.viewmodel.AlarmVM

@Composable
fun AlarmDetailScreen(
    onNavigate: NavHostController,
    longitude: String,
    latitude: String
) {
    SetSideEffect(onNavigate)
    val viewModel = hiltViewModel<AlarmVM>()
    val address = viewModel.findAddress.collectAsState()

    if (longitude.isNotEmpty() && latitude.isNotEmpty()) {
        viewModel.getAddress(longitude, latitude)
    }

    if (address.value != null) {
        AlarmDetailCompose(address.value)
    }
}

@Composable
fun AlarmDetailCompose(address: Address?) {
    address?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(text = "지번 주소\n${it.jibunAddress.orEmpty()}")
            Text(text = "도로명 주소\n${it.roadAddress.orEmpty()}")
            FixedMarkerMap(it)
        }
    }
}