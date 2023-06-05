package com.gps_alarm.ui.viewmodel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.data.gpsAlarm.local.LocalDataStore
import com.domain.gpsAlarm.usecase.DataStoreUseCase
import com.gps_alarm.base.BaseVM
import com.gps_alarm.data.Address
import com.gps_alarm.data.MapData
import com.gps_alarm.data.SettingInfo
import com.gps_alarm.ui.map.CustomNaverMaps
import com.gps_alarm.ui.util.decode
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.InfoWindow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MapVM @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase,
    private val json: Json
) : ContainerHost<MapData, MapVM.MapSideEffect>, BaseVM() {
    override val container: Container<MapData, MapSideEffect> = container(MapData())
    lateinit var customMapView: CustomNaverMaps

    sealed class MapSideEffect {
    }

    fun createMap(context: Context) {
        intent {
            viewModelScope.launch(Dispatchers.Main) {
                reduce { state.copy(mapView = null, alarmList = null, loading = true, error = null) }
                val alarmList = withContext(Dispatchers.IO) { dataStoreUseCase.get(LocalDataStore.Keys.alarmList)?.map { it.decode<Address>(json) } ?: emptyList() }
                val settingInfo = withContext(Dispatchers.IO) { dataStoreUseCase.get(LocalDataStore.Keys.setting)?.decode<SettingInfo>(json) ?: SettingInfo() }
                reduce {
                    state.copy(
                        mapView = createMapView(context, settingInfo.isFollowing),
                        alarmList = alarmList,
                        settingInfo = settingInfo,
                        loading = false,
                        error = null
                    )
                }
            }
        }
    }

    private fun createMapView(context: Context, isFollowing: Boolean): MapView {
        customMapView = CustomNaverMaps(
            context,
            null
        ).setMapSettings {
            it.locationTrackingMode = LocationTrackingMode.Follow
            it.isIndoorEnabled = true
            it.moveCamera(CameraUpdate.zoomTo(16.0))
            it.uiSettings.apply {
                isLocationButtonEnabled = true
                isScrollGesturesEnabled = true
                isCompassEnabled = true
                isIndoorLevelPickerEnabled = true
            }
        }.setInfoWindow(
            object : InfoWindow.DefaultTextAdapter(context) {
                override fun getText(infoWindow: InfoWindow): CharSequence {
                    return infoWindow.marker?.tag as? CharSequence ?: ""
                }
            }
        ).setCameraFollowing(isFollowing)

        return customMapView.create()
    }
}