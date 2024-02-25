package com.bowoon.commonutils

import android.os.Build

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