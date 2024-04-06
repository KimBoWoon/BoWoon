package com.bowoon.imageloader

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException

interface ImageLoadListener {
    fun onStart()
    fun onFailed(e: GlideException?, model: Any?)
    fun onSuccess(resource: Drawable?, model: Any?, dataSource: DataSource?)
}