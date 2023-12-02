package com.data.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmapOrNull
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

object ImageLoader {
    fun load(
        context: Context,
        url: String,
        width: Int = Target.SIZE_ORIGINAL,
        height: Int = Target.SIZE_ORIGINAL,
        imageView: ImageView
    ) {
        Glide.with(context)
            .load(url)
            .override(width, height)
            .into(imageView)
    }

    fun loadBitmap(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context)
            .asBitmap()
            .load(url)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("width : ${resource?.width}, height : ${resource?.height}, size : ${resource?.byteCount}")
                    return false
                }
            })
            .into(imageView)
    }

    fun loadDrawable(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context)
            .asDrawable()
            .load(url)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("width : ${resource?.toBitmapOrNull()?.width}, height : ${resource?.toBitmapOrNull()?.height}, size : ${resource?.toBitmapOrNull()?.byteCount}")
                    return false
                }
            })
            .into(imageView)
    }

    fun loadFile(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context)
            .asFile()
            .load(url)
            .listener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: File?,
                    model: Any?,
                    target: Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
//                    Log.d("width : ${resource?.width}, height : ${resource?.height}, size : ${resource?.length()}")
                    Log.d("size : ${resource?.length()}")
                    return false
                }
            })
            .into(imageView)
    }

    fun loadGif(
        context: Context,
        url: String,
        imageView: ImageView
    ) {
        Glide.with(context)
            .asGif()
            .load(url)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.d("width : ${resource?.toBitmapOrNull()?.width}, height : ${resource?.toBitmapOrNull()?.height}, size : ${resource?.toBitmapOrNull()?.byteCount}")
                    return false
                }
            })
            .into(imageView)
    }

//    fun <T> getRequestListener(type: Type) =
//        when (type) {
//            Type.BITMAP -> {
//                object : RequestListener<Bitmap> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Bitmap>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onResourceReady(
//                        resource: Bitmap?,
//                        model: Any?,
//                        target: Target<Bitmap>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//                }
//            }
//            Type.FILE -> {
//                object : RequestListener<File> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<File>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onResourceReady(
//                        resource: File?,
//                        model: Any?,
//                        target: Target<File>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//                }
//            }
//            Type.DRAWABLE -> {
//                object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//                }
//            }
//            Type.GIF -> {
//                object : RequestListener<GifDrawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<GifDrawable>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//
//                    override fun onResourceReady(
//                        resource: GifDrawable?,
//                        model: Any?,
//                        target: Target<GifDrawable>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        TODO("Not yet implemented")
//                    }
//                }
//            }
//        }

    enum class Type {
        BITMAP, FILE, DRAWABLE, GIF
    }

    private val listener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            TODO("Not yet implemented")
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            TODO("Not yet implemented")
        }
    }
}