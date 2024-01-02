package com.bowoon.imageloader

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bowoon.commonutils.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

object ImageLoader {
    private const val TAG = "#ImageLoader"

    fun load(
        context: Context,
        imageView: ImageView,
        source: Any,
        option: ImageOptions? = null
    ) {
        Glide.with(context)
            .load(source)
            .apply(option ?: RequestOptions())
            .listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.printStackTrace(e)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.d(TAG, "model > [$model], dataSource > [$dataSource], width > [${resource?.intrinsicWidth}], height > [${resource?.intrinsicHeight}]")
                        return false
                    }
                }
            )
            .into(imageView)
    }

    fun download(
        context: Context,
        source: String
    ) {
        Glide.with(context)
            .download(source)
            .submit()
            .get()
    }
}