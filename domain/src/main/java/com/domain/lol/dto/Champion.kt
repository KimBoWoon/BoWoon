package com.domain.lol.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ChampionRoot(
    val type: String? = null,
    val format: String? = null,
    val version: String? = null,
    val `data`: Map<String, ChampionInfo>? = null
) : Parcelable

@Keep
@Parcelize
data class ChampionInfo(
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