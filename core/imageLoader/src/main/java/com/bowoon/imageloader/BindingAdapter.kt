package com.bowoon.imageloader

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bowoon.commonutils.Log
import com.bumptech.glide.request.target.Target

object BindingAdapter {
    @JvmStatic
    @BindingAdapter(
        "loadImage",
        "width",
        "height",
        "placeholder",
        "placeholderDrawable",
        "error",
        "errorDrawable",
        "radius",
        "topLeft",
        "topRight",
        "bottomLeft",
        "bottomRight",
        requireAll = false
    )
    fun ImageView?.loadImage(
        source: String?,
        width: Int?,
        height: Int?,
        placeholder: Int?,
        error: Int?,
        placeholderDrawable: Drawable?,
        errorDrawable: Drawable?,
        radius: Int?,
        topLeft: Float?,
        topRight: Float?,
        bottomLeft: Float?,
        bottomRight: Float?
    ) {
        this ?: run {
            Log.e("ImageView is null")
            return
        }
        source ?: run {
            Log.e("url is null")
            return
        }

        ImageLoader.load(
            this.context,
            this,
            source,
            ImageOptions(
                width = width ?: Target.SIZE_ORIGINAL,
                height = height ?: Target.SIZE_ORIGINAL,
                placeholder = placeholder,
                error = error,
                placeholderDrawable = placeholderDrawable,
                errorDrawable = errorDrawable,
                radius = radius,
                topLeftRadius = topLeft ?: 0f,
                topRightRadius = topRight ?: 0f,
                bottomLeftRadius = bottomLeft ?: 0f,
                bottomRightRadius = bottomRight ?: 0f
            )
        )
    }
}