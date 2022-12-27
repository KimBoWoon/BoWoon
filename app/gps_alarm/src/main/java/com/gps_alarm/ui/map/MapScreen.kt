package com.gps_alarm.ui.map

import android.app.Activity
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.util.FusedLocationSource
import kotlinx.coroutines.launch

@Composable
fun MapScreen(onNavigate: NavHostController) {
    MapsCompose()
}

@Composable
fun MapsCompose() {
    var map: NaverMap
    var fusedLocationSource: FusedLocationSource
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val mapView = remember {
        MapView(context).apply {
            getMapAsync { naverMap ->
                fusedLocationSource = FusedLocationSource(context as Activity, 1000)
                map = naverMap.apply {
                    locationSource = fusedLocationSource
                    locationTrackingMode = LocationTrackingMode.Follow
                    uiSettings.isLocationButtonEnabled = true
                    addOnLocationChangeListener { location ->
                        val coord = LatLng(location)
                        locationOverlay.apply {
                            isVisible = true
                            position = coord
                            bearing = location.bearing
                        }
                        moveCamera(CameraUpdate.scrollTo(coord))
                        moveCamera(CameraUpdate.zoomTo(16.0))
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
    AndroidView(factory = { mapView })
}