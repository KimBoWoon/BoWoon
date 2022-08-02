package com.domain.lol.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class ChampionRoot(
    @SerialName("type")
    val type: String? = null,
    @SerialName("format")
    val format: String? = null,
    @SerialName("version")
    val version: String? = null,
    @SerialName("data")
    val `data`: Map<String, ChampionInfo>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class ChampionInfo(
    @SerialName("blurb")
    val blurb: String? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("image")
    val image: Image? = null,
    @SerialName("info")
    val info: Info? = null,
    @SerialName("key")
    val key: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("partype")
    val partype: String? = null,
    @SerialName("stats")
    val stats: Stats? = null,
    @SerialName("tags")
    val tags: List<String?>? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("version")
    val version: String? = null
) : Parcelable