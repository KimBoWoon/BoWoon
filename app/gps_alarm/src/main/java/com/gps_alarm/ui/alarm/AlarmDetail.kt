package com.gps_alarm.ui.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gps_alarm.data.Address
import com.gps_alarm.ui.viewmodel.AlarmVM
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import util.Log

@Composable
fun AlarmDetailScreen(
    onNavigate: NavHostController,
    addressId: Int,
    viewModel: AlarmVM = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    var address by remember { mutableStateOf<Address?>(null) }

    if (addressId != -1) {
        LaunchedEffect(
            key1 = scope,
            block = {
                scope.launch {
                    address = withContext(scope.coroutineContext) { viewModel.getAddress(addressId) }
                    Log.d(address.toString())
                }
            })
    }

    AlarmDetailCompose(address)
}

@Composable
fun AlarmDetailCompose(address: Address?) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        content = { paddingValues ->
            address?.let {
                Column(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Text(text = it.jibunAddress.orEmpty())
                    Text(text = it.roadAddress.orEmpty())
                }
            }
        }
    )
}