package com.gps_alarm.ui.alarm

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.gps_alarm.data.Address
import com.gps_alarm.ui.NavigationScreen
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import com.gps_alarm.ui.theme.Purple700
import com.gps_alarm.ui.util.OnLifecycleEvent
import com.gps_alarm.ui.viewmodel.AlarmVM
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    val listState = rememberLazyListState()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.fetchAlarmList()
        })
    val fabVisibility by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    val density = LocalDensity.current
    val context = LocalContext.current

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> { Log.d("ON_START") }
            Lifecycle.Event.ON_CREATE -> {
                Log.d("ON_CREATE")
                viewModel.container.sideEffectFlow
                    .onEach { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                    .launchIn(owner.lifecycleScope)
            }
            Lifecycle.Event.ON_RESUME -> {
                Log.d("ON_RESUME")
                viewModel.fetchAlarmList()
            }
            Lifecycle.Event.ON_PAUSE -> { Log.d("ON_PAUSE") }
            Lifecycle.Event.ON_STOP -> { Log.d("ON_STOP") }
            Lifecycle.Event.ON_DESTROY -> { Log.d("ON_DESTROY") }
            Lifecycle.Event.ON_ANY -> { Log.d("ON_ANY") }
        }
    }

    val state = viewModel.container.stateFlow.collectAsState().value
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .pullRefresh(pullRefreshState),
        contentAlignment = Alignment.TopCenter
    ) {
        when {
            state.loading -> {
                Log.d("alarm list geocode data loading...")
                CircularProgressIndicator(
                    modifier = Modifier.align(alignment = Alignment.Center)
                )
            }
            state.alarmList.isEmpty() && state.error == null -> {
                isRefreshing = false
                Text(text = "저장된 주소가 없습니다.")
            }
            state.alarmList.isNotEmpty() && state.error == null -> {
                isRefreshing = false
                AlarmContent(onNavigate, listState, state.alarmList)
            }
            else -> {
                GpsAlarmDialog(
                    "데이터를 가져오는대 문제가 발생했습니다.\n다시 시도하시겠습니까?",
                    "재시도",
                    { viewModel.fetchAlarmList() },
                    "취소",
                    {}
                )
            }
        }
        PullRefreshIndicator(refreshing = isRefreshing, state = pullRefreshState)

        AnimatedVisibility(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .padding(bottom = 10.dp, end = 10.dp)
                .align(alignment = Alignment.BottomEnd),
            visible = fabVisibility,
            enter = slideInVertically {
                with(density) { 40.dp.roundToPx() }
            } + fadeIn(),
            exit = fadeOut(
                animationSpec = keyframes {
                    this.durationMillis = 120
                }
            )
        ) {
            FloatingActionButton(
                onClick = { onNavigate.navigate(NavigationScreen.CreateAlarm.route) },
                backgroundColor = Purple700,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, "add alarm")
            }
        }
    }
}

@Composable
fun AlarmContent(
    onNavigate: NavHostController,
    listState: LazyListState,
    addresses: List<Address>
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(addresses) { index, address ->
            AddressItem(onNavigate, address, addresses.lastIndex == index)
        }
    }
}

@Composable
fun AddressItem(
    onNavigate: NavHostController,
    address: Address,
    isLast: Boolean
) {
    val viewModel: AlarmVM = hiltViewModel()
    var checkedState by remember { mutableStateOf(address.isEnable ?: false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = if (isLast) 10.dp else 0.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable { onNavigate.navigate("${NavigationScreen.AlarmDetail.route}/${address.longitude}/${address.latitude}") },
    ) {
        Box(
            modifier = Modifier.padding(5.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(text = address.roadAddress ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(text = address.jibunAddress ?: "", maxLines = 1, overflow = TextOverflow.Ellipsis)
                Switch(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                        address.isEnable = it
                        viewModel.changeData(address)
                    }
                )
            }
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .align(alignment = Alignment.BottomEnd),
                onClick = {
                    viewModel.deleteAlarm(address.longitude, address.latitude)
                }
            ) {
                Text(text = "제거")
            }
        }
    }
}