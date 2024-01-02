package com.bowoon.imageloader

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.bowoon.commonutils.ScreenUtils.dp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target

data class ImageOptions(
    private val diskCacheStrategy: DiskCacheStrategy = DiskCacheStrategy.NONE,
    private val skipMemoryCache: Boolean = true,
    private val width: Int = Target.SIZE_ORIGINAL,
    private val height: Int = Target.SIZE_ORIGINAL,
    private val placeholderDrawable: Drawable? = null,
    @DrawableRes val placeholder: Int? = null,
    private val errorDrawable: Drawable? = null,
    @DrawableRes val error: Int? = null,
    private val topLeftRadius: Float = -1f,
    private val topRightRadius: Float = -1f,
    private val bottomLeftRadius: Float = -1f,
    private val bottomRightRadius: Float = -1f,
    private val radius: Int? = null
): RequestOptions() {
    init {
        diskCacheStrategy(diskCacheStrategy)
        skipMemoryCache(skipMemoryCache)
        override(width, height)

        placeholder?.let { drawableId ->
            placeholder(drawableId)
        } ?: run {
            placeholder(placeholderDrawable)
        }

        error?.let { errorId ->
            error(errorId)
        } ?: run {
            error(errorDrawable)
        }

        when {
            radius != null -> transform(RoundedCorners(radius.dp))
            topLeftRadius != -1f || topRightRadius != -1f || bottomLeftRadius != -1f || bottomRightRadius != -1f ->
                transform(GranularRoundedCorners(topLeftRadius, topRightRadius, bottomRightRadius, bottomLeftRadius))
            else -> null
        }
    }
}