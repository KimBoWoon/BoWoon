package com.domain.lol.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class ChampionDetailRoot(
    @SerialName("data")
    val `data`: Map<String, ChampionDetail>? = null,
    @SerialName("format")
    val format: String? = null,
    @SerialName("type")
    val type: String? = null,
    @SerialName("version")
    val version: String? = null
) : Parcelable {
    init {
        data?.values?.forEach { championDetail ->
            championDetail.version = version ?: ""
        }
    }
}

@Keep
@Serializable
@Parcelize
data class ChampionDetail(
    @SerialName("allytips")
    val allytips: List<String?>? = null,
    @SerialName("blurb")
    val blurb: String? = null,
    @SerialName("enemytips")
    val enemytips: List<String?>? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("image")
    val image: Image? = null,
    @SerialName("info")
    val info: Info? = null,
    @SerialName("key")
    val key: String? = null,
    @SerialName("lore")
    val lore: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("partype")
    val partype: String? = null,
    @SerialName("passive")
    val passive: Passive? = null,
//            @SerialName("recommended")
//            val recommended: List<Any?>? = null,
    @SerialName("skins")
    val skins: List<Skin?>? = null,
    @SerialName("spells")
    val spells: List<Spell?>? = null,
    @SerialName("stats")
    val stats: ChampionStats? = null,
    @SerialName("tags")
    val tags: List<String?>? = null,
    @SerialName("title")
    val title: String? = null
) : Parcelable {
    var version = ""
    fun getReplaceLore(): String = lore?.replace(" ", "\u00A0") ?: ""
}

@Keep
@Serializable
@Parcelize
data class Passive(
    @SerialName("description")
    val description: String? = null,
    @SerialName("image")
    val image: Image? = null,
    @SerialName("name")
    val name: String? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Skin(
    @SerialName("chromas")
    val chromas: Boolean? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("num")
    val num: Int? = null
) : Parcelable {
    var championName = ""

    fun getChampionSkinImageUrl(): String =
        "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${championName}_${num}.jpg"
}

@Keep
@Serializable
@Parcelize
data class Spell(
    @SerialName("cooldown")
    val cooldown: List<Float?>? = null,
    @SerialName("cooldownBurn")
    val cooldownBurn: String? = null,
    @SerialName("cost")
    val cost: List<Int?>? = null,
    @SerialName("costBurn")
    val costBurn: String? = null,
    @SerialName("costType")
    val costType: String? = null,
    @SerialName("datavalues")
    val datavalues: Datavalues? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("effect")
    val effect: List<List<Float?>?>? = null,
    @SerialName("effectBurn")
    val effectBurn: List<String?>? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("image")
    val image: Image? = null,
    @SerialName("leveltip")
    val leveltip: Leveltip? = null,
    @SerialName("maxammo")
    val maxammo: String? = null,
    @SerialName("maxrank")
    val maxrank: Int? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("range")
    val range: List<Int?>? = null,
    @SerialName("rangeBurn")
    val rangeBurn: String? = null,
    @SerialName("resource")
    val resource: String? = null,
    @SerialName("tooltip")
    val tooltip: String? = null,
//                @SerialName("vars")
//                val vars: List<Any?>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
class Datavalues : Parcelable

@Keep
@Serializable
@Parcelize
data class Leveltip(
    @SerialName("effect")
    val effect: List<String?>? = null,
    @SerialName("label")
    val label: List<String?>? = null
) : Parcelable