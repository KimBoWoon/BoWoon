package com.domain.lol.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ChampionDetailRoot(
    val `data`: Map<String, ChampionDetail>? = null,
    val format: String? = null,
    val type: String? = null,
    val version: String? = null
) : Parcelable {
    init {
        data?.values?.forEach { championDetail ->
            championDetail.version = version ?: ""
        }
    }
}

@Keep
@Parcelize
data class ChampionDetail(
    val allytips: List<String?>? = null,
    val blurb: String? = null,
    val enemytips: List<String?>? = null,
    val id: String? = null,
    val image: Image? = null,
    val info: Info? = null,
    val key: String? = null,
    val lore: String? = null,
    val name: String? = null,
    val partype: String? = null,
    val passive: Passive? = null,
//            val recommended: List<Any?>? = null,
    val skins: List<Skin?>? = null,
    val spells: List<Spell?>? = null,
    val stats: ChampionStats? = null,
    val tags: List<String?>? = null,
    val title: String? = null
) : Parcelable {
    var version = ""
    fun getReplaceLore(): String = lore?.replace(" ", "\u00A0") ?: ""
    fun getAllytipsString(): String {
        var result = ""
        allytips?.forEach {
            result += "$it\n"
        }
        return result
    }
    fun getEnemytipsString(): String {
        var result = ""
        enemytips?.forEach {
            result += "$it\n"
        }
        return result
    }
}

@Keep
@Parcelize
data class Passive(
    val description: String? = null,
    val image: Image? = null,
    val name: String? = null
) : Parcelable

@Keep
@Parcelize
data class Skin(
    val chromas: Boolean? = null,
    val id: String? = null,
    val name: String? = null,
    val num: Int? = null
) : Parcelable {
    var championName = ""

    fun getChampionSkinImageUrl(): String =
        "https://ddragon.leagueoflegends.com/cdn/img/champion/splash/${championName}_${num}.jpg"
}

@Keep
@Parcelize
data class Spell(
    val cooldown: List<Float?>? = null,
    val cooldownBurn: String? = null,
    val cost: List<Int?>? = null,
    val costBurn: String? = null,
    val costType: String? = null,
    val datavalues: Datavalues? = null,
    val description: String? = null,
    val effect: List<List<Float?>?>? = null,
    val effectBurn: List<String?>? = null,
    val id: String? = null,
    val image: Image? = null,
    val leveltip: Leveltip? = null,
    val maxammo: String? = null,
    val maxrank: Int? = null,
    val name: String? = null,
    val range: List<Int?>? = null,
    val rangeBurn: String? = null,
    val resource: String? = null,
    val tooltip: String? = null,
//                val vars: List<Any?>? = null
) : Parcelable

@Keep
@Parcelize
class Datavalues : Parcelable

@Keep
@Parcelize
data class Leveltip(
    val effect: List<String?>? = null,
    val label: List<String?>? = null
) : Parcelable