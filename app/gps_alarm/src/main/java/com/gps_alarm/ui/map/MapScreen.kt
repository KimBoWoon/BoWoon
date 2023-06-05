package com.gps_alarm.ui.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.gps_alarm.data.MapData
import com.gps_alarm.ui.util.OnLifecycleEvent
import com.gps_alarm.ui.util.addCircleOverlay
import com.gps_alarm.ui.util.addMarker
import com.gps_alarm.ui.viewmodel.MapVM
import kotlinx.coroutines.launch
import util.Log

@Composable
fun MapScreen(onNavigate: NavHostController) {
    SetSideMapEffect()
    InitLifecycle()
    InitMapScreen()
}

@Composable
fun InitLifecycle() {
    val context = LocalContext.current
    val viewModel = hiltViewModel<MapVM>()

    OnLifecycleEvent { owner, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                owner.lifecycle.addObserver(viewModel)
                viewModel.createMap(context)
            }
            Lifecycle.Event.ON_START -> {}
            Lifecycle.Event.ON_RESUME -> {
                owner.lifecycle.addObserver(viewModel.customMapView)
            }
            Lifecycle.Event.ON_PAUSE -> {}
            Lifecycle.Event.ON_STOP -> {}
            Lifecycle.Event.ON_DESTROY -> {}
            Lifecycle.Event.ON_ANY -> {}
        }
    }
}

@Composable
fun SetSideMapEffect() {
    val viewModel = hiltViewModel<MapVM>()
    val lifecycle = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect("key") {
        lifecycle.lifecycleScope.launch {
            viewModel.container.sideEffectFlow.flowWithLifecycle(lifecycle.lifecycle, Lifecycle.State.CREATED).collect {

            }
        }
    }
}

@Composable
fun InitMapScreen() {
    val viewModel = hiltViewModel<MapVM>()
    val state = viewModel.container.stateFlow.collectAsState(MapData(null, null, null, true, null)).value

    when {
        state.loading -> {
            Log.d("addressList Loading...")
        }
        state.mapView != null -> {
            if (!state.alarmList.isNullOrEmpty()) {
                state.mapView.getMapAsync { naverMap ->
                    state.alarmList.filter { it.isEnable == true }.forEach { address ->
                        addMarker(address).apply {map = naverMap }
                        addCircleOverlay(address, state.settingInfo?.circleSize?.toDouble() ?: 0.0).apply { map = naverMap }
                    }
                }
            }
            AndroidView(factory = { state.mapView })
        }
        state.error != null -> {
            Log.printStackTrace(state.error)
        }
        else -> {
            Log.d("MapScreen unknown > $state")
        }
    }
}