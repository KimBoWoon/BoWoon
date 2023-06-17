package com.gps_alarm.ui.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import util.Log

@Composable
fun CheckPermission() {
    val permissionList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS,
            Manifest.permission.FOREGROUND_SERVICE,
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.FOREGROUND_SERVICE,
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    }
    val context = LocalContext.current
    val showDialogList = mutableListOf<String>()
    var permissionDenied = 0

    permissionList.forEach { permission ->
        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {
            showDialogList.add(permission)
        } else if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            permissionDenied++
        }
    }

    Log.i("permission denied > ${permissionDenied}, show dialog > ${showDialogList.size}")

    when {
        permissionDenied > 0 -> AskForPermissions(permissionList)
        showDialogList.size > 0 -> GoToAppSetting()
        else -> Log.d("all permission granted")
    }
}

@Composable
private fun AskForPermissions(permissionList: Array<String>) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.entries.forEach {
                Log.d("permission status > ${it.key}, ${it.value}")
            }
        }
    )
    LaunchedEffect("key") { launcher.launch(permissionList) }
}

@Composable
fun GoToAppSetting() {
    val context = LocalContext.current

    GpsAlarmDialog(
        message = "앱을 사용하기위해 위치와 알림 권한이 필요합니다.\n권한설정을 위해 환경설정으로 이동하시겠습니까?",
        confirmText = "이동",
        confirmCallback = {
            Log.d("권한 확인을 위해 환경설정 이동")
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).run {
                data = Uri.parse("package:${context.packageName}")
                addCategory(Intent.CATEGORY_DEFAULT)
                context.startActivity(this)
            }
        },
        dismissText = "취소",
        dismissCallback = {
            Log.d("환경설정으로 이동 안함")
        }
    )
}