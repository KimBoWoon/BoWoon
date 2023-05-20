package com.gps_alarm.ui.alarm

import android.app.Activity
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.domain.gpsAlarm.dto.Addresses
import com.gps_alarm.ui.util.OnLifecycleEvent
import com.gps_alarm.ui.util.ShowSnackbar
import com.gps_alarm.ui.viewmodel.AlarmVM
import com.gps_alarm.ui.webview.ShowWebView
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch
import util.Log

@Composable
fun CreateAlarmScreen(onNavigate: NavHostController) {
    CreateAlarmCompose(onNavigate)
}

@Composable
fun CreateAlarmCompose(onNavigate: NavHostController, viewModel: AlarmVM = hiltViewModel()) {
    var showDialog by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var alarmTitle by remember { mutableStateOf("") }
    val geocode by viewModel.geocode.collectAsState()

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_START -> { Log.d("ON_START") }
            Lifecycle.Event.ON_CREATE -> {
                Log.d("ON_CREATE")
                owner.lifecycleScope.launchWhenCreated {
                    viewModel.container.sideEffectFlow.collect {
                        when (it) {
                            is AlarmVM.AlarmSideEffect.AddAlarm -> {
                                viewModel.setDataStore(it.data)
                            }
                            else -> {
                                Log.d("CreateAlarmScreen > not support $it")
                            }
                        }
                    }
                }
            }
            Lifecycle.Event.ON_RESUME -> { Log.d("ON_RESUME") }
            Lifecycle.Event.ON_PAUSE -> { Log.d("ON_PAUSE") }
            Lifecycle.Event.ON_STOP -> { Log.d("ON_STOP") }
            Lifecycle.Event.ON_DESTROY -> { Log.d("ON_DESTROY") }
            Lifecycle.Event.ON_ANY -> { Log.d("ON_ANY") }
        }
    }

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
                onValueChange = {
                    viewModel.alarmTitle.value = it
                    alarmTitle = it
                }
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
                    if (geocode?.addresses.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(horizontal = 10.dp),
                            text = "해당 주소를 찾을 수 없습니다."
                        )
                    } else {
                        CreateAddressList(geocode?.addresses)
                    }
                }
                "INVALID_REQUEST", "SYSTEM_ERROR" -> {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(horizontal = 10.dp),
                        text = geocode?.errorMessage ?: "something wrong..."
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
                    if (viewModel.chooseAddress.value != null && viewModel.alarmTitle.value.isNotEmpty()) {
                        viewModel.addAlarm(viewModel.alarmTitle.value, viewModel.chooseAddress.value)
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
fun CreateAddressList(addresses: List<Addresses>?) {
    val viewModel: AlarmVM = hiltViewModel()
    var chooseAddress by remember { mutableStateOf<Addresses?>(null) }
    val confirmCallback: (Addresses?) -> (Unit) = {
        viewModel.chooseAddress.value = it
        chooseAddress = null
    }
    val dismissCallback = {
        viewModel.chooseAddress.value = null
        chooseAddress = null
    }

    addresses?.let {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp)
        ) {
            items(it) { address ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            chooseAddress = address
                            viewModel.chooseAddress.value = address
                        }
                ) {
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
    } ?: run {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp),
            text = "해당 주소를 찾을 수 없습니다."
        )
    }

    if (chooseAddress != null) {
        MapDialog(chooseAddress, confirmCallback, dismissCallback)
    }
}

@Composable
fun MapDialog(addresses: Addresses?, confirmCallback: (Addresses?) -> Unit, dismissCallback: () -> Unit) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    Dialog(
        onDismissRequest = { dismissCallback.invoke() },
        properties = DialogProperties(false, false)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                Text(
                    modifier = Modifier
                        .weight(2f)
                        .align(alignment = Alignment.CenterVertically),
                    text = "해당 주소가 맞는지 확인하세요."
                )
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { confirmCallback.invoke(addresses) }
                ) {
                    Text(text = "확인")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = { dismissCallback.invoke() }
                ) {
                    Text(text = "취소")
                }
            }
            val fusedLocationSource = FusedLocationSource(context as Activity, 1000)
            val mapView = remember {
                val naverMapOptions = NaverMapOptions().apply {
                    camera(CameraPosition(LatLng(addresses?.latitude ?: 0.0, addresses?.longitude ?: 0.0), 16.0))
                }
                MapView(context, naverMapOptions).apply {
                    getMapAsync { naverMap ->
                        naverMap.apply {
                            locationSource = fusedLocationSource
                            locationTrackingMode = LocationTrackingMode.None
                            isIndoorEnabled = true
                            uiSettings.apply {
                                isLocationButtonEnabled = false
                                isScrollGesturesEnabled = false
                            }

                            if (addresses?.latitude != null && addresses.longitude != null) {
                                Marker().apply {
                                    position = LatLng(addresses.latitude ?: 0.0, addresses.longitude ?: 0.0)
                                    width = Marker.SIZE_AUTO
                                    height = Marker.SIZE_AUTO
                                    map = naverMap
                                }
                            }
                        }
                    }
                }
            }
            val lifecycleObserver = remember {
                LifecycleEventObserver { source, event ->
                    // CoroutineScope 안에서 호출해야 정상적으로 동작합니다.
                    coroutineScope.launch {
                        when (event) {
                            Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                            Lifecycle.Event.ON_START -> mapView.onStart()
                            Lifecycle.Event.ON_RESUME -> mapView.onResume()
                            Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                            Lifecycle.Event.ON_STOP -> mapView.onStop()
                            Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                            Lifecycle.Event.ON_ANY -> TODO()
                        }
                    }
                }
            }

            // 뷰가 해제될 때 이벤트 리스너를 제거합니다.
            DisposableEffect(true) {
                lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
                }
            }

            // 생성된 MapView 객체를 AndroidView로 Wrapping 합니다.
            AndroidView(
                modifier = Modifier.fillMaxWidth().height(500.dp),
                factory = { mapView }
            )
        }
    }
}

@Composable
fun AddressDialog(dismissDialogCallback: () -> Unit) {
    Dialog(onDismissRequest = { dismissDialogCallback.invoke() }) {
//        val local = "file:///android_asset/address.html"
//        val local = "http://10.0.2.2/address.html"
        val local = "http://172.30.50.236/address.html"
        ShowWebView(local, dismissDialogCallback)
    }
}