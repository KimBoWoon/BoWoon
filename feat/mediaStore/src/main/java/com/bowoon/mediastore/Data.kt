package com.bowoon.mediastore

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

sealed interface MediaDataClass {
    val uri: Uri?
    val name: String?
    val size: String?
    val mime: String?
    val extension: String?
}

@Parcelize
data class Image(
    override val uri: Uri? = null,
    override val name: String? = null,
    override val size: String? = null,
    override val mime: String? = null,
    override val extension: String? = null,
) : MediaDataClass, Parcelable

@Parcelize
data class Video(
    override val uri: Uri? = null,
    override val name: String? = null,
    override val size: String? = null,
    override val mime: String? = null,
    override val extension: String? = null,
    val duration: Long? = null
) : MediaDataClass, Parcelable

@Parcelize
data class Audio(
    override val uri: Uri? = null,
    override val name: String? = null,
    override val size: String? = null,
    override val mime: String? = null,
    override val extension: String? = null,
    val duration: Long? = null
) : MediaDataClass, Parcelable

@Parcelize
data class File(
    override val uri: Uri? = null,
    override val name: String? = null,
    override val size: String? = null,
    override val mime: String? = null,
    override val extension: String? = null
) : MediaDataClass, Parcelable

data class FileInfo(
    val name: String? = null,
    val mimeType: String? = null,
    val size: String? = null,
    val duration: Long? = null
)

@Parcelize
data class ChooseItemList(
    val list: @RawValue List<MediaDataClass>? = null
) : Parcelable