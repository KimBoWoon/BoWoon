package com.bowoon.lol.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class Image(
    val full: String? = null,
    val group: String? = null,
    val h: Int? = null,
    val sprite: String? = null,
    val w: Int? = null,
    val x: Int? = null,
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
    val attack: Int? = null,
    val defense: Int? = null,
    val difficulty: Int? = null,
    val magic: Int? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class ChampionStats(
    val armor: Float? = null,
    val armorperlevel: Float? = null,
    val attackdamage: Float? = null,
    val attackdamageperlevel: Float? = null,
    val attackrange: Float? = null,
    val attackspeed: Float? = null,
    val attackspeedperlevel: Float? = null,
    val crit: Float? = null,
    val critperlevel: Float? = null,
    val hp: Float? = null,
    val hpperlevel: Float? = null,
    val hpregen: Float? = null,
    val hpregenperlevel: Float? = null,
    val movespeed: Float? = null,
    val mp: Float? = null,
    val mpperlevel: Float? = null,
    val mpregen: Float? = null,
    val mpregenperlevel: Float? = null,
    val spellblock: Float? = null,
    val spellblockperlevel: Float? = null
) : Parcelable