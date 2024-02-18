package com.bowoon.fileprovider

import android.os.Parcelable
import com.bowoon.mediastore.Image
import com.bowoon.mediastore.Video
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseItemList(
    val imageList: List<Image>? = null,
    val videoList: List<Video>? = null
) : Parcelable