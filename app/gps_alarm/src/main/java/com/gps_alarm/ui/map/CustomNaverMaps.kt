package com.gps_alarm.ui.map

import android.app.Activity
import android.content.Context
import androidx.core.os.bundleOf
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.NaverMapOptions
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.InfoWindow
import com.naver.maps.map.util.FusedLocationSource
import util.Log

class CustomNaverMaps(
    private val context: Context,
    private val options: NaverMapOptions? = null
) : DefaultLifecycleObserver {
    companion object {
        var infoWindow: InfoWindow? = null
    }

    private val maps = MapView(context, options)
    private val followingListener = NaverMap.OnLocationChangeListener { location ->
        maps.getMapAsync { naverMap ->
            val coord = LatLng(location)
            naverMap.locationOverlay.apply {
                isVisible = true
                position = coord
                bearing = location.bearing
            }

            naverMap.moveCamera(CameraUpdate.scrollTo(coord))
            naverMap.moveCamera(CameraUpdate.zoomTo(16.0))
        }
    }

    fun setMapSettings(callback: OnMapReadyCallback): CustomNaverMaps {
        maps.getMapAsync(callback)
        return this
    }

    fun setInfoWindow(adapter: InfoWindow.DefaultTextAdapter): CustomNaverMaps {
        infoWindow = InfoWindow(adapter)
        return this
    }

    fun setCameraFollowing(isFollowing: Boolean): CustomNaverMaps {
        maps.getMapAsync { naverMap ->
            Log.d("isFollowing > $isFollowing")
            naverMap.removeOnLocationChangeListener(followingListener)
            if (isFollowing) {
                naverMap.addOnLocationChangeListener(followingListener)
            }
        }
        return this
    }

    fun addLocationChangeListener(listener: NaverMap.OnLocationChangeListener? = null): CustomNaverMaps {
        maps.getMapAsync { naverMap ->
            naverMap.addOnLocationChangeListener { location ->
                listener?.onLocationChange(location)
            }
        }
        return this
    }

    fun create(): MapView =
        maps.apply {
            getMapAsync { naverMap ->
                naverMap.apply {
                    locationSource = FusedLocationSource(context as Activity, 1000)

                    setOnMapClickListener { pointF, latLng ->
                        infoWindow?.close()
                    }
                }
            }
        }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        maps.onCreate(bundleOf())
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        maps.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        maps.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        maps.onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

        maps.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        maps.onDestroy()
    }
}