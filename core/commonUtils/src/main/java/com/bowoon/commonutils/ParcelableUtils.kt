package com.bowoon.commonutils

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Bundle.getSafetySerializable(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> getSerializable(key) as? T
    }

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Bundle.getSafetyParcelable(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
        else -> getParcelable(key) as? T
    }

@Suppress("DEPRECATION")
inline fun <reified T : Parcelable> Intent.getSafetyParcelableExtra(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
        else -> getParcelableExtra(key)
    }

@Suppress("DEPRECATION")
inline fun <reified T : Serializable> Intent.getSafetySerializableExtra(key: String): T? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(key, T::class.java)
        else -> getSerializableExtra(key) as? T
    }

inline fun <reified T : Parcelable> Intent.getSafetyParcelableArrayExtra(key: String): Array<T>? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArrayExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayExtra(key) as? Array<T>
    }

inline fun <reified T : Parcelable> Bundle.getSafetyParcelableArrayExtra(key: String): Array<T>? =
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableArray(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArray(key) as? Array<T>
    }