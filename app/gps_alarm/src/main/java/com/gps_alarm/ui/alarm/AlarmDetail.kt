package com.gps_alarm.ui.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gps_alarm.paging.room.entity.Address
import com.gps_alarm.ui.viewmodel.AlarmVM
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.Log

@Composable
fun AlarmDetailScreen(onNavigate: NavHostController, addressId: Int, viewModel: AlarmVM = hiltViewModel()) {
    val scope = rememberCoroutineScope()
    var address by remember { mutableStateOf<Address?>(null) }

    if (addressId != -1) {
        scope.launch {
            address = withContext(scope.coroutineContext) { viewModel.getAddress(addressId) }
            Log.d(address.toString())
        }
    }

    AlarmDetailCompose(address)
}

@Composable
fun AlarmDetailCompose(address: Address?) {
    address?.let {
        Column {
            Text(text = it.jibunAddress.orEmpty())
            Text(text = it.roadAddress.orEmpty())
        }
    }
}