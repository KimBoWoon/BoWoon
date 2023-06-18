package com.gps_alarm.ui.util

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Intent.parcelableArray(key: String): Array<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, Array<T>::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArrayExtra(key) as? Array<T>
}

inline fun <reified T : Parcelable> Bundle.parcelableArray(key: String): Array<T>? = when {
    Build.VERSION.SDK_INT >= 33 -> getParcelableArray(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableArray(key) as? Array<T>
}