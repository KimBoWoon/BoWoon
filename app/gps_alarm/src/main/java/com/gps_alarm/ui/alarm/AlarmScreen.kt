package com.gps_alarm.ui.alarm

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.gps_alarm.paging.room.entity.Address
import com.gps_alarm.ui.Screen
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import com.gps_alarm.ui.theme.Purple700
import com.gps_alarm.ui.viewmodel.AlarmVM

@Composable
fun AlarmScreen(onNavigate: NavHostController) {
    AlarmCompose(onNavigate)
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalPagingApi::class)
@Composable
fun AlarmCompose(
    onNavigate: NavHostController,
    viewModel: AlarmVM = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        content = {
            val geocodeList = viewModel.pager.collectAsLazyPagingItems()

            var isRefreshing by remember { mutableStateOf(false) }
            val pullRefreshState = rememberPullRefreshState(
                refreshing = isRefreshing,
                onRefresh = {
                    isRefreshing = true
                    geocodeList.refresh()
                })

            Box(modifier = Modifier.pullRefresh(pullRefreshState), contentAlignment = Alignment.TopCenter) {
                AlarmContent(geocodeList)
                PullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState)
            }

            LaunchedEffect(geocodeList.loadState) {
//                when (geocodeList.loadState.prepend) {
//                    is LoadState.Loading -> { isRefreshing = true }
//                    is LoadState.NotLoading -> { isRefreshing = false }
//                    is LoadState.Error -> { isRefreshing = false }
//                    else -> { isRefreshing = false }
//                }
                when (geocodeList.loadState.refresh) {
                    is LoadState.Loading -> {  }
                    is LoadState.NotLoading -> { isRefreshing = false }
                    is LoadState.Error -> {
                        showDialog = true
                        isRefreshing = false
                    }
                    else -> { isRefreshing = false }
                }
                when (geocodeList.loadState.append) {
                    is LoadState.Loading -> {  }
                    is LoadState.NotLoading -> { isRefreshing = false }
                    is LoadState.Error -> {
                        isRefreshing = false
                        showDialog = true
                    }
                    else -> { isRefreshing = false }
                }
            }

            if (showDialog) {
                GpsAlarmDialog(
                    "dialog",
                    "재시도",
                    { geocodeList.refresh() },
                    "취소",
                    {}
                )
            }
        },
        floatingActionButton = { floatingActionButtons(onNavigate) }
    )
}

@Composable
fun AlarmContent(addressesList: LazyPagingItems<Address>) {
    val state = rememberLazyListState()

    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(addressesList) { index, addresses ->
            addresses?.let {
                AddressItem(addresses)
            }
        }
    }
}

@Composable
fun AddressItem(addresses: Address) {
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

//@Preview(
//    name = "main screen preview",
//    showBackground = true
//)
//@Composable
//fun Preview() {
//    AlarmCompose()
//}