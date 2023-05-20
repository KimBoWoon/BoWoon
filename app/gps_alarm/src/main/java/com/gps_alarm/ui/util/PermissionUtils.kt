package com.gps_alarm.ui.util

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.gps_alarm.ui.dialog.GpsAlarmDialog
import util.Log

@Composable
fun CheckPermission() {
    val permissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS
    )
    val context = LocalContext.current
    val showDialogList = mutableListOf<String>()
    var permissionDenied = 0

    permissionList.forEach {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, it)) {
            showDialogList.add(it)
        } else if (ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED) {
            permissionDenied++
        }
    }

    Log.i("permission denied > ${permissionDenied}, show dialog > ${showDialogList.size}")

    when {
        showDialogList.size > 0 -> {
            //Some permissions are denied and can be asked again.
            AskForPermissions(permissionList)
        }
        permissionDenied > 0 -> {
            //Show alert dialog
            GoToAppSetting()
        }
        else -> {
            //All permissions granted. Do your stuff 🤞
            Log.d("all permission granted")
        }
    }
}

@Composable
private fun AskForPermissions(permissionsList: Array<String>) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.keys.forEach {
                if (permissions[it] == true) {
                    Log.d("permission granted")
                } else if (permissions[it] == false) {
                    Log.d("permission denied")
                } else {
                    Log.d("permission null")
                }
            }
        }
    )
    val newPermissionStr = mutableListOf<String>()
    permissionsList.forEach {
        newPermissionStr.add(it)
    }
    if (newPermissionStr.isNotEmpty()) {
        SideEffect { launcher.launch(newPermissionStr.toTypedArray()) }
    } else {
        /* User has pressed 'Deny & Don't ask again' so we have to show the enable permissions dialog
        which will lead them to app details page to enable permissions from there. */
        GoToAppSetting()
    }
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