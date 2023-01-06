package com.gps_alarm.ui.alarm

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gps_alarm.ui.util.ShowSnackbar
import com.gps_alarm.ui.viewmodel.AlarmVM
import com.gps_alarm.ui.webview.ShowWebView

@Composable
fun CreateAlarmScreen(onNavigate: NavHostController) {
    CreateAlarmCompose(onNavigate)
}

@Composable
fun CreateAlarmCompose(onNavigate: NavHostController, viewModel: AlarmVM = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var alarmTitle by remember { mutableStateOf("") }
    val geocode = viewModel.geocode.value

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
                singleLine = true,
                maxLines = 1,
                label = { Text(text = "알람 이름") },
                value = alarmTitle,
                onValueChange = { alarmTitle = it }
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp),
                onClick = {
                    showDialog = true
                }) {
                Text(text = "주소찾기")
            }
            when (geocode?.status) {
                "OK" -> {
                    geocode.addresses?.let {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 10.dp)
                        ) {
                            itemsIndexed(it) { index, address ->
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(horizontal = 10.dp),
                                    text = address.jibunAddress ?: ""
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight()
                                        .padding(horizontal = 10.dp),
                                    text = address.roadAddress ?: ""
                                )
                            }
                        }
                    }
                }
                "INVALID_REQUEST", "SYSTEM_ERROR" -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp),
                        text = geocode.errorMessage ?: "something wrong..."
                    )
                }
                else -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp),
                        text = "검색된 주소의 리스트가 나타납니다.\n주소를 검색해보세요!"
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(alignment = Alignment.BottomStart),
        ) {
            Button(
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(start = 10.dp, end = 5.dp),
                onClick = {
                    if (!geocode?.addresses.isNullOrEmpty() && alarmTitle.isNotEmpty()) {
                        viewModel.setDataStore(alarmTitle, geocode?.addresses)
                        onNavigate.navigateUp()
                    } else {
                        showSnackbar = true
                    }
                }
            ) {
                Text(text = "저장")
            }
            Button(
                modifier = Modifier
                    .weight(weight = 1f)
                    .padding(start = 5.dp, end = 10.dp),
                onClick = { onNavigate.navigateUp() }
            ) {
                Text(text = "취소")
            }
        }
    }

    if (showDialog) {
        AddressDialog(dismissDialogCallback = { showDialog = false })
    }

    if (showSnackbar) {
        ShowSnackbar(
            message = "주소가 제대로 입력되지 않았습니다.",
            dismissSnackbarCallback = { showSnackbar = false }
        )
    }
}

@Composable
fun AddressDialog(dismissDialogCallback: () -> Unit) {
    Dialog(onDismissRequest = { dismissDialogCallback.invoke() }) {
        val local = "http://10.0.2.2/address.html"
        val other = "http://192.168.35.128/address.html"
        ShowWebView(other, dismissDialogCallback)
    }
}