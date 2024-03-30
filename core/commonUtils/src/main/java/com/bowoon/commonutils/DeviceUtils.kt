package com.bowoon.commonutils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings


fun toApi(version: Int, include: Boolean = true): Boolean =
    when (Build.VERSION.SDK_INT < version || (include && Build.VERSION.SDK_INT == version)) {
        true -> true
        false -> false
    }

fun fromApi(version: Int, include: Boolean = true): Boolean =
    when (Build.VERSION.SDK_INT > version || (include && Build.VERSION.SDK_INT == version)) {
        true -> true
        false -> false
    }

fun Context.getDeviceUniqueId(): String =
    Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
////        this.deviceId
//        ""
//    } else {
//        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//    }

fun Context.getVersionName(flags: Int = 0): String =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        else -> packageManager.getPackageInfo(packageName, flags)
    }.run {
        versionName
    }

fun Context.getVersionCode(flags: Int = 0): Long =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
        else -> packageManager.getPackageInfo(packageName, flags)
    }.run {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            longVersionCode
        } else {
            versionCode.toLong()
        }
    }

//fun getVersionName(context: Context, flags: Int = 0): String =
//    when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
//        else -> context.packageManager.getPackageInfo(context.packageName, flags)
//    }.run {
//        versionName
//    }
//
//fun getVersionCode(context: Context, flags: Int = 0): Long =
//    when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
//        else -> context.packageManager.getPackageInfo(context.packageName, flags)
//    }.run {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            longVersionCode
//        } else {
//            versionCode.toLong()
//        }
//    }