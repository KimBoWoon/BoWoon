package com.gps_alarm.ui.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gps_alarm.data.Address
import com.gps_alarm.ui.util.dpToSp
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
        topBar = { AlarmDetailTopBar() },
        content = {
            address?.let {
                Column {
                    Text(text = it.jibunAddress.orEmpty())
                    Text(text = it.roadAddress.orEmpty())
                }
            }
        }
    )
}

@Composable
fun AlarmDetailTopBar() {
    TopAppBar(
        title = { Text(text = "알람 세부 내용", color = Color.White, fontSize = dpToSp(20.dp)) },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = Color.White,
        elevation = 2.dp,
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
    )
}