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
import com.gps_alarm.ui.util.addCircleOverlay
import com.gps_alarm.ui.util.addMarker
import com.gps_alarm.ui.viewmodel.MapVM
import kotlinx.coroutines.launch
import util.Log

@Composable
fun MapScreen(onNavigate: NavHostController) {
    SetSideMapEffect()
    val viewModel = hiltViewModel<MapVM>()
    val state = viewModel.container.stateFlow.collectAsState().value

    viewModel.getSetting()

    when {
        state.loading -> {
            Log.d("addressList Loading...")
        }
        state.mapView != null -> {
            if (!state.alarmList.isNullOrEmpty()) {
                state.mapView.getMapAsync { naverMap ->
                    state.alarmList.forEach { address ->
                        addMarker(address).apply {
                            map = if (address.isEnable == true) {
                                naverMap
                            } else {
                                null
                            }
                        }
                        addCircleOverlay(address).apply {
                            map = if (address.isEnable == true) {
                                naverMap
                            } else {
                                null
                            }
                        }
                    }
                }
            }
            AndroidView(factory = { state.mapView })
        }
        state.error != null -> {
            Log.printStackTrace(state.error)
        }
        else -> {
            Log.d("MapScreen unknown")
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
                when (it) {
                    is MapVM.MapSideEffect.GetSetting -> {
                        viewModel.getIsFollowing(context)
                    }
                }
            }
        }
    }
}