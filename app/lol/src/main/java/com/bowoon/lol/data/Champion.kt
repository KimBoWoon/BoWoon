package com.bowoon.lol.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class ChampionData(
    val type: String? = null,
    val format: String? = null,
    val version: String? = null,
    val `data`: Map<String, Champion>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Champion(
    val blurb: String? = null,
    val id: String? = null,
    val image: Image? = null,
    val info: Info? = null,
    val key: String? = null,
    val name: String? = null,
    val partype: String? = null,
    val stats: ChampionStats? = null,
    val tags: List<String?>? = null,
    val title: String? = null,
    val version: String? = null
) : Parcelable {
    init {
        image?.version = version ?: ""
    }
}