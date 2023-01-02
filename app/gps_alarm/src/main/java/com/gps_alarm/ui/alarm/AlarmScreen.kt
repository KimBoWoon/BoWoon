package com.gps_alarm.ui.alarm

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.gps_alarm.ui.NavigationScreen
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import com.gps_alarm.ui.theme.Purple700
import com.gps_alarm.ui.viewmodel.AlarmVM
import util.DataStatus
import util.Log

@Composable
fun AlarmScreen(onNavigate: NavHostController) {
    AlarmCompose(onNavigate)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AlarmCompose(
    onNavigate: NavHostController,
    viewModel: AlarmVM = hiltViewModel()
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    var showDialog by remember { mutableStateOf(false) }

    val geocodeList by viewModel.geocodeList.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.setList()
        })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        when (geocodeList) {
            is DataStatus.Loading -> {
                Log.d("alarm list geocode data loading...")
//                        isRefreshing = true
            }
            is DataStatus.Success -> {
                AlarmContent(onNavigate, (geocodeList as? DataStatus.Success)?.data ?: emptyList())
                isRefreshing = false
            }
            is DataStatus.Failure -> {
                isRefreshing = false
                if (!showDialog) {
                    showDialog = true

                    GpsAlarmDialog(
                        "데이터를 가져오는대 문제가 발생했습니다.\n다시 시도하시겠습니까?",
                        "재시도",
                        {
                            showDialog = false
                            viewModel.setList()
                        },
                        "취소",
                        { showDialog = false }
                    )
                }
            }
        }
        PullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState)

        FloatingActionButton(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(bottom = 10.dp, end = 10.dp)
                .align(alignment = Alignment.BottomEnd),
            onClick = { onNavigate.navigate(NavigationScreen.CreateAlarm.route) },
            backgroundColor = Purple700,
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, "add alarm")
        }
    }
}

@Composable
fun AlarmContent(
    onNavigate: NavHostController,
    addressesList: List<com.gps_alarm.data.Address>
) {
    val state = rememberLazyListState()

    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(addressesList) { index, addresses ->
            AddressItem(onNavigate, addresses)
        }
    }
}

@Composable
fun AddressItem(
    onNavigate: NavHostController,
    addresses: com.gps_alarm.data.Address
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable { onNavigate.navigate("${NavigationScreen.AlarmDetail.route}/${addresses.longitude}/${addresses.latitude}") },
    ) {
        Column(
            modifier = Modifier.padding(5.dp)
        ) {
            Text(text = addresses.roadAddress ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = addresses.jibunAddress ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}