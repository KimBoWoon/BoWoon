package com.bowoon.permissionmanager

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.requestPermission(
    granted: (() -> Unit)? = null,
    denied: (() -> Unit)? = null
): ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            granted?.invoke()
        } else {
            denied?.invoke()
        }
    }

fun FragmentActivity.requestMultiplePermission(
    granted: (() -> Unit)? = null,
    denied: (() -> Unit)? = null
): ActivityResultLauncher<Array<String>> =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
        if (permissionMap.values.any { !it }) {
            denied?.invoke()
        } else {
            granted?.invoke()
        }
    }

fun Fragment.requestPermission(
    granted: (() -> Unit)? = null,
    denied: (() -> Unit)? = null
): ActivityResultLauncher<String> =
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            granted?.invoke()
        } else {
            denied?.invoke()
        }
    }

fun Fragment.requestMultiplePermission(
    granted: (() -> Unit)? = null,
    denied: (() -> Unit)? = null
): ActivityResultLauncher<Array<String>> =
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionMap ->
        if (permissionMap.values.any { !it }) {
            denied?.invoke()
        } else {
            granted?.invoke()
        }
    }

fun Context.requestPermission(
    permission: String,
    launcher: ActivityResultLauncher<String>
) {
    when {
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED -> {
            // You can use the API that requires the permission.
        }
        ActivityCompat.shouldShowRequestPermissionRationale(this as FragmentActivity, permission) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            // showInContextUI(...)
        }
        else -> {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            launcher.launch(permission)
        }
    }
}

fun Context.requestMultiplePermission(
    permissions: Array<String>,
    launcher: ActivityResultLauncher<Array<String>>
) {
    when {
        permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED } -> {
            // You can use the API that requires the permission.
        }
        permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(this as FragmentActivity, it) } -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            // showInContextUI(...)
        }
        else -> {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            launcher.launch(permissions)
        }
    }
}