package com.domain.lol.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class Image(
    @SerialName("full")
    val full: String? = null,
    @SerialName("group")
    val group: String? = null,
    @SerialName("h")
    val h: Int? = null,
    @SerialName("sprite")
    val sprite: String? = null,
    @SerialName("w")
    val w: Int? = null,
    @SerialName("x")
    val x: Int? = null,
    @SerialName("y")
    val y: Int? = null
) : Parcelable {
    var version = ""

    private fun getImageUrl(): String =
        "https://ddragon.leagueoflegends.com/cdn/$version/img"

    fun getChampionSquareImage(): String =
        "${getImageUrl()}/champion/$full"

    fun getGameItemImage(): String =
        "${getImageUrl()}/item/$full"

    fun getChampionSpellImage(): String =
        "${getImageUrl()}/spell/${full}"

    fun getChampionPassiveImage(): String =
        "${getImageUrl()}/passive/${full}"
}

@Keep
@Serializable
@Parcelize
data class Info(
    @SerialName("attack")
    val attack: Int? = null,
    @SerialName("defense")
    val defense: Int? = null,
    @SerialName("difficulty")
    val difficulty: Int? = null,
    @SerialName("magic")
    val magic: Int? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class ChampionStats(
    @SerialName("armor")
    val armor: Float? = null,
    @SerialName("armorperlevel")
    val armorperlevel: Float? = null,
    @SerialName("attackdamage")
    val attackdamage: Float? = null,
    @SerialName("attackdamageperlevel")
    val attackdamageperlevel: Float? = null,
    @SerialName("attackrange")
    val attackrange: Float? = null,
    @SerialName("attackspeed")
    val attackspeed: Float? = null,
    @SerialName("attackspeedperlevel")
    val attackspeedperlevel: Float? = null,
    @SerialName("crit")
    val crit: Float? = null,
    @SerialName("critperlevel")
    val critperlevel: Float? = null,
    @SerialName("hp")
    val hp: Float? = null,
    @SerialName("hpperlevel")
    val hpperlevel: Float? = null,
    @SerialName("hpregen")
    val hpregen: Float? = null,
    @SerialName("hpregenperlevel")
    val hpregenperlevel: Float? = null,
    @SerialName("movespeed")
    val movespeed: Float? = null,
    @SerialName("mp")
    val mp: Float? = null,
    @SerialName("mpperlevel")
    val mpperlevel: Float? = null,
    @SerialName("mpregen")
    val mpregen: Float? = null,
    @SerialName("mpregenperlevel")
    val mpregenperlevel: Float? = null,
    @SerialName("spellblock")
    val spellblock: Float? = null,
    @SerialName("spellblockperlevel")
    val spellblockperlevel: Float? = null
) : Parcelable