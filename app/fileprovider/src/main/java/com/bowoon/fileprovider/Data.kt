package com.bowoon.fileprovider

import android.os.Parcelable
import com.bowoon.mediastore.Image
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChooseItemList(
    val list: List<Image>? = null
) : Parcelable