package com.bowoon.mediastore

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.io.Serializable

sealed interface MediaDataClass

data class Image(
    val uri: String? = null,
    val name: String? = null,
    val size: Int? = null
) : MediaDataClass, Serializable

data class Video(
    val uri: String? = null,
    val name: String? = null,
    val duration: Int? = null,
    val size: Int? = null
) : MediaDataClass, Serializable

data class Audio(
    val uri: String? = null,
    val name: String? = null,
    val duration: Int? = null,
    val size: Int? = null
) : MediaDataClass, Serializable

data class File(
    val uri: String? = null,
    val name: String? = null,
    val size: Int? = null
) : MediaDataClass, Serializable

@Parcelize
data class ChooseItemList(
    val list: @RawValue List<MediaDataClass>? = null
//    val imageList: List<Image>? = null,
//    val videoList: List<Video>? = null,
//    val audioList: List<Audio>? = null,
//    val fileList: List<File>? = null
) : Parcelable