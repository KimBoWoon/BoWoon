package com.gps_alarm.ui.alarm

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.ui.activities.Screen
import com.gps_alarm.ui.fragments.AlarmVM
import com.gps_alarm.ui.theme.Purple700
import com.gps_alarm.ui.util.dpToSp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import util.DataStatus
import util.Log

@Composable
fun AlarmScreen(onNavigate: NavHostController) {
    AlarmCompose(onNavigate)
}

@Composable
fun AlarmCompose(
    onNavigate: NavHostController,
    viewModel: AlarmVM = hiltViewModel()
) {
    Scaffold(
        content = {
            it.toString()
            val state by viewModel.geocodeList.collectAsState()

            when (state) {
                is DataStatus.Loading -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator()
                    }
                }
                is DataStatus.Success -> {
                    val data = (state as? DataStatus.Success<List<Addresses>>)?.data
                    if (data.isNullOrEmpty()) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = "저장된 데이터가 없습니다.",
                                fontSize = dpToSp(20.dp),
                                color = Color.Black
                            )
                        }
                    } else {
                        AlarmContent(addressesList = (state as DataStatus.Success<List<Addresses>>).data)
                    }
                }
                is DataStatus.Failure -> {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = (state as? DataStatus.Failure)?.throwable?.message ?: "Something wrong...",
                            fontSize = dpToSp(20.dp),
                            color = Color.Black
                        )
                    }
                }
            }
        },
        floatingActionButton = { floatingActionButtons(onNavigate) }
    )
}

@Composable
fun AlarmContent(addressesList: List<Addresses>) {
    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LazyColumn(state = state) {
        addressesList.let { alarmList ->
            itemsIndexed(addressesList) { index, addresses ->
                addresses.let {
                    val clickEvent = {
                        scope.launch {
                            state.scrollToItem(index)
                        }
                    }

                    val modifier = Modifier.padding(
                        start = 8.dp,
                        top = if (index == 0) 8.dp else 4.dp,
                        end = 8.dp,
                        bottom = if (index == alarmList.lastIndex) 8.dp else 4.dp
                    )
                    AddressItem(addresses)
                }
            }
        }
    }
}

class SampleAddressProvider: PreviewParameterProvider<Addresses> {
    override val values = sequenceOf(
        Addresses(
            null,
            20.toDouble(),
            "englishAddress",
            "jibunAddress",
            "roadAddress",
            36.toDouble(),
            78.toDouble()
        )
    )
}

@Composable
fun AddressItem(@PreviewParameter(SampleAddressProvider::class) addresses: Addresses) {
    Card(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp)
            .border(width = 2.dp, color = Color.Black)
    ) {
        Column() {
            Text(text = addresses.roadAddress ?: "")
            Text(text = addresses.jibunAddress ?: "")
        }
    }
}


@Composable
fun floatingActionButtons(onNavigate: NavHostController) {
    FloatingActionButton(
        onClick = { onNavigate.navigate(Screen.CreateAlarm.route) },
        backgroundColor = Purple700,
        contentColor = Color.White
    ) {
        Icon(Icons.Filled.Add, "add alarm")
    }
}

@Composable
fun ShowSnackbar(
    message: String,
    actionLabel: String
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        coroutineScope.launch {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel
            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> Log.d("SnackbarResult Dismissed")
                SnackbarResult.ActionPerformed -> Log.d("SnackbarResult ActionPerformed")
            }
        }
    }
}

//@Preview(
//    name = "main screen preview",
//    showBackground = true
//)
//@Composable
//fun Preview() {
//    AlarmCompose()
//}