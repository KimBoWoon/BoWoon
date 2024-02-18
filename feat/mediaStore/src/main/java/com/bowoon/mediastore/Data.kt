package com.bowoon.mediastore

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

data class Image(
    val uri: String? = null,
    val name: String? = null,
    val size: Int? = null
) : Serializable

data class Video(
    val uri: String? = null,
    val name: String? = null,
    val duration: Int? = null,
    val size: Int? = null
) : Serializable

data class Audio(
    val uri: String? = null,
    val name: String? = null,
    val duration: Int? = null,
    val size: Int? = null
)

@Parcelize
data class ChooseItemList(
    val list: List<Image>? = null
) : Parcelable