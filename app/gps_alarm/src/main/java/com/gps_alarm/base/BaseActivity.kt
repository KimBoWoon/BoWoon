package com.gps_alarm.base

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.CallSuper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import util.ContextUtils.showToast
import util.Log

abstract class BaseActivity : ComponentActivity() {
    companion object {
        private const val TAG = "BaseActivity"
        const val PERMISSION_REQUEST_CODE = 1000
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * 권한을 얻었을 경우
     */
    abstract fun permissionGranted(requestCode: Int)

    /**
     * 권한을 얻지 못했을 경우
     */
    abstract fun permissionDenied(requestCode: Int)

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "권한 승인")
            permissionGranted(PERMISSION_REQUEST_CODE)
        } else {
            Log.d(TAG, "권한 거부")
            permissionDenied(PERMISSION_REQUEST_CODE)
        }
    }

    fun requirePermissions(permissions: Array<String>) {
        permissions.forEach { permission ->
            when (PackageManager.PERMISSION_DENIED) {
                ContextCompat.checkSelfPermission(this, permission) -> {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        requestPermissionLauncher.launch(permission)
                    } else {
                        ActivityCompat.requestPermissions(this@BaseActivity, permissions, PERMISSION_REQUEST_CODE)
                    }
                }
                else -> {
                    requestPermissionLauncher.launch(permission)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        grantResults.forEachIndexed { index, result ->
            if (result == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "${permissions[index]} 권한 승인")
            } else {
                Log.d(TAG, "${permissions[index]} 권한 거부")
                showToast("해당 기능을 사용하기 위해 권한이 필요합니다.", Toast.LENGTH_SHORT)
            }
        }
    }
}