package com.gps_alarm.ui.alarm

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.gps_alarm.data.Address
import com.gps_alarm.data.AlarmData
import com.gps_alarm.ui.NavigationScreen
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import com.gps_alarm.ui.util.OnLifecycleEvent
import com.gps_alarm.ui.viewmodel.AlarmVM
import kotlinx.coroutines.launch
import util.Log

@Composable
fun AlarmScreen(onNavigate: NavHostController) {
    SetSideEffect(onNavigate)
    InitAlarmScreen(onNavigate)
}

@Composable
fun SetSideEffect(
    onNavigate: NavHostController
) {
    val viewModel = hiltViewModel<AlarmVM>()
    val context = LocalContext.current

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                Log.d("ON_CREATE")

                owner.lifecycleScope.launch {
                    owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.container.sideEffectFlow.collect {
                            when (it) {
                                is AlarmVM.AlarmSideEffect.ShowToast -> {
                                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                }
                                is AlarmVM.AlarmSideEffect.SaveListToDataStore -> {
                                    viewModel.saveToDataStore(it.data)
                                }
                                is AlarmVM.AlarmSideEffect.ModifyAddress -> {
                                    viewModel.changeData(it.data)
                                }
                                is AlarmVM.AlarmSideEffect.GoToDetail -> {
                                    onNavigate.navigate("${NavigationScreen.AlarmDetail.route}/${it.longitude}/${it.latitude}")
                                }
                                is AlarmVM.AlarmSideEffect.AddAlarm -> {
                                    viewModel.setDataStore(it.data)
                                    onNavigate.popBackStack()
                                }
                                is AlarmVM.AlarmSideEffect.GetGeocode -> {
                                    viewModel.geocode.value = it.geocode
                                }
                                is AlarmVM.AlarmSideEffect.GetAddress -> {
                                    it.address?.let { address ->
                                        Log.d(address.toString())
                                        viewModel.findAddress.value = address
                                    } ?: run {
                                        Log.d("잘못된 데이터입니다!")
                                        onNavigate.popBackStack()
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Lifecycle.Event.ON_START -> { Log.d("ON_START") }
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
}

@Composable
fun InitAlarmScreen(onNavigate: NavHostController) {
    val viewModel = hiltViewModel<AlarmVM>()
    val listState = rememberLazyListState()
    var isRefreshing by remember { mutableStateOf(false) }
    val alarmData = viewModel.container.stateFlow.collectAsState(AlarmData(alarmList = null, loading = true, error = null)).value
    @OptIn(ExperimentalMaterialApi::class)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            viewModel.fetchAlarmList()
        })

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        @OptIn(ExperimentalMaterialApi::class)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(1f)
                .pullRefresh(pullRefreshState),
            contentAlignment = Alignment.TopCenter
        ) {
            when {
                alarmData.loading -> {
                    Log.d("alarm list geocode data loading...")
                    Text(text = "데이터를 로드 중 입니다...")
                    CircularProgressIndicator(
                        modifier = Modifier.align(alignment = Alignment.Center)
                    )
                }
                alarmData.alarmList?.isEmpty() == true && alarmData.error == null -> {
                    Log.d("alarm list geocode data empty")
                    isRefreshing = false
                    Text(text = "저장된 주소가 없습니다.")
                }
                alarmData.alarmList?.isNotEmpty() == true && alarmData.error == null -> {
                    Log.d("alarm list geocode data success")
                    isRefreshing = false
                    AlarmContent(listState, alarmData.alarmList)
                }
                alarmData.error != null -> {
                    Log.d("alarm list geocode data error")
                    GpsAlarmDialog(
                        "데이터를 가져오는대 문제가 발생했습니다.\n다시 시도하시겠습니까?",
                        "재시도",
                        { viewModel.fetchAlarmList() },
                        "취소",
                        {}
                    )
                }
                else -> {
                    Log.d("alarm list geocode data unknown")
                }
            }
            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = isRefreshing,
                state = pullRefreshState
            )
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp, 0.dp, 16.dp, 0.dp),
            onClick = { onNavigate.navigate(NavigationScreen.CreateAlarm.route) }
        ) {
            Text(text = "알람 추가")
        }
    }
}

@Composable
fun AlarmContent(
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
            AddressItem(address, addresses.lastIndex == index)
        }
    }
}

@Composable
fun AddressItem(
    address: Address,
    isLast: Boolean
) {
    val viewModel = hiltViewModel<AlarmVM>()
    var checkedState by remember { mutableStateOf(address.isEnable ?: false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = if (isLast) 10.dp else 0.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
            .clickable {
                viewModel.goToDetail(address.longitude.toString(), address.latitude.toString())
            }
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
                        viewModel.modifyAddress(address)
                    }
                )
            }
            Button(
                modifier = Modifier
                    .wrapContentSize()
                    .align(alignment = Alignment.BottomEnd),
                onClick = {
                    viewModel.removeAlarm(address.longitude, address.latitude)
                }
            ) {
                Text(text = "제거")
            }
        }
    }
}