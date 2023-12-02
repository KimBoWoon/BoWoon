package com.data.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlin.reflect.KProperty

class ViewInflater<V : ViewDataBinding>(
    private val inflater: LayoutInflater,
    @LayoutRes private val layoutId: Int,
    private val parent: ViewGroup? = null,
    private val attachToParent: Boolean = false,
    private val settingCallback: (V.() -> Unit)? = null
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return DataBindingUtil.inflate<V>(inflater, layoutId, parent, attachToParent).apply {
            settingCallback?.invoke(this)
        }
    }

//    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: V) {}
}