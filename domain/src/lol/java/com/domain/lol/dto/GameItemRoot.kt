package com.domain.lol.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Serializable
@Parcelize
data class GameItemRoot(
    @SerialName("basic")
    val basic: Basic? = null,
    @SerialName("data")
    val `data`: Map<String, GameItemInfo>? = null,
    @SerialName("groups")
    val groups: List<Group?>? = null,
    @SerialName("tree")
    val tree: List<Tree?>? = null,
    @SerialName("type")
    val type: String? = null, // item
    @SerialName("version")
    val version: String? = null // 12.14.1
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Basic(
    @SerialName("colloq")
    val colloq: String? = null, // ;
    @SerialName("consumeOnFull")
    val consumeOnFull: Boolean? = null, // false
    @SerialName("consumed")
    val consumed: Boolean? = null, // false
    @SerialName("depth")
    val depth: Int? = null, // 1
    @SerialName("description")
    val description: String? = null,
    @SerialName("from")
    val from: List<String?>? = null,
    @SerialName("gold")
    val gold: Gold? = null,
    @SerialName("group")
    val group: String? = null,
    @SerialName("hideFromAll")
    val hideFromAll: Boolean? = null, // false
    @SerialName("inStore")
    val inStore: Boolean? = null, // true
    @SerialName("into")
    val into: List<String?>? = null,
    @SerialName("maps")
    val maps: Map<String, Boolean>? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("plaintext")
    val plaintext: String? = null,
    @SerialName("requiredAlly")
    val requiredAlly: String? = null,
    @SerialName("requiredChampion")
    val requiredChampion: String? = null,
    @SerialName("rune")
    val rune: Rune? = null,
    @SerialName("specialRecipe")
    val specialRecipe: Int? = null, // 0
    @SerialName("stacks")
    val stacks: Int? = null, // 1
    @SerialName("stats")
    val stats: GameItemStats? = null,
    @SerialName("tags")
    val tags: List<String?>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Rune(
    @SerialName("isrune")
    val isrune: Boolean? = null, // false
    @SerialName("tier")
    val tier: Int? = null, // 1
    @SerialName("type")
    val type: String? = null // red
) : Parcelable

@Keep
@Serializable
@Parcelize
data class GameItemStats(
    @SerialName("FlatArmorMod")
    val flatArmorMod: Float? = null, // 0
    @SerialName("FlatAttackSpeedMod")
    val flatAttackSpeedMod: Float? = null, // 0
    @SerialName("FlatBlockMod")
    val flatBlockMod: Float? = null, // 0
    @SerialName("FlatCritChanceMod")
    val flatCritChanceMod: Float? = null, // 0
    @SerialName("FlatCritDamageMod")
    val flatCritDamageMod: Float? = null, // 0
    @SerialName("FlatEXPBonus")
    val flatEXPBonus: Float? = null, // 0
    @SerialName("FlatEnergyPoolMod")
    val flatEnergyPoolMod: Float? = null, // 0
    @SerialName("FlatEnergyRegenMod")
    val flatEnergyRegenMod: Float? = null, // 0
    @SerialName("FlatHPPoolMod")
    val flatHPPoolMod: Float? = null, // 0
    @SerialName("FlatHPRegenMod")
    val flatHPRegenMod: Float? = null, // 0
    @SerialName("FlatMPPoolMod")
    val flatMPPoolMod: Float? = null, // 0
    @SerialName("FlatMPRegenMod")
    val flatMPRegenMod: Float? = null, // 0
    @SerialName("FlatMagicDamageMod")
    val flatMagicDamageMod: Float? = null, // 0
    @SerialName("FlatMovementSpeedMod")
    val flatMovementSpeedMod: Float? = null, // 0
    @SerialName("FlatPhysicalDamageMod")
    val flatPhysicalDamageMod: Float? = null, // 0
    @SerialName("FlatSpellBlockMod")
    val flatSpellBlockMod: Float? = null, // 0
    @SerialName("PercentArmorMod")
    val percentArmorMod: Float? = null, // 0
    @SerialName("PercentAttackSpeedMod")
    val percentAttackSpeedMod: Float? = null, // 0
    @SerialName("PercentBlockMod")
    val percentBlockMod: Float? = null, // 0
    @SerialName("PercentCritChanceMod")
    val percentCritChanceMod: Float? = null, // 0
    @SerialName("PercentCritDamageMod")
    val percentCritDamageMod: Float? = null, // 0
    @SerialName("PercentDodgeMod")
    val percentDodgeMod: Float? = null, // 0
    @SerialName("PercentEXPBonus")
    val percentEXPBonus: Float? = null, // 0
    @SerialName("PercentHPPoolMod")
    val percentHPPoolMod: Float? = null, // 0
    @SerialName("PercentHPRegenMod")
    val percentHPRegenMod: Float? = null, // 0
    @SerialName("PercentLifeStealMod")
    val percentLifeStealMod: Float? = null, // 0
    @SerialName("PercentMPPoolMod")
    val percentMPPoolMod: Float? = null, // 0
    @SerialName("PercentMPRegenMod")
    val percentMPRegenMod: Float? = null, // 0
    @SerialName("PercentMagicDamageMod")
    val percentMagicDamageMod: Float? = null, // 0
    @SerialName("PercentMovementSpeedMod")
    val percentMovementSpeedMod: Float? = null, // 0
    @SerialName("PercentPhysicalDamageMod")
    val percentPhysicalDamageMod: Float? = null, // 0
    @SerialName("PercentSpellBlockMod")
    val percentSpellBlockMod: Float? = null, // 0
    @SerialName("PercentSpellVampMod")
    val percentSpellVampMod: Float? = null, // 0
    @SerialName("rFlatArmorModPerLevel")
    val rFlatArmorModPerLevel: Float? = null, // 0
    @SerialName("rFlatArmorPenetrationMod")
    val rFlatArmorPenetrationMod: Float? = null, // 0
    @SerialName("rFlatArmorPenetrationModPerLevel")
    val rFlatArmorPenetrationModPerLevel: Float? = null, // 0
    @SerialName("rFlatCritChanceModPerLevel")
    val rFlatCritChanceModPerLevel: Float? = null, // 0
    @SerialName("rFlatCritDamageModPerLevel")
    val rFlatCritDamageModPerLevel: Float? = null, // 0
    @SerialName("rFlatDodgeMod")
    val rFlatDodgeMod: Float? = null, // 0
    @SerialName("rFlatDodgeModPerLevel")
    val rFlatDodgeModPerLevel: Float? = null, // 0
    @SerialName("rFlatEnergyModPerLevel")
    val rFlatEnergyModPerLevel: Float? = null, // 0
    @SerialName("rFlatEnergyRegenModPerLevel")
    val rFlatEnergyRegenModPerLevel: Float? = null, // 0
    @SerialName("rFlatGoldPer10Mod")
    val rFlatGoldPer10Mod: Float? = null, // 0
    @SerialName("rFlatHPModPerLevel")
    val rFlatHPModPerLevel: Float? = null, // 0
    @SerialName("rFlatHPRegenModPerLevel")
    val rFlatHPRegenModPerLevel: Float? = null, // 0
    @SerialName("rFlatMPModPerLevel")
    val rFlatMPModPerLevel: Float? = null, // 0
    @SerialName("rFlatMPRegenModPerLevel")
    val rFlatMPRegenModPerLevel: Float? = null, // 0
    @SerialName("rFlatMagicDamageModPerLevel")
    val rFlatMagicDamageModPerLevel: Float? = null, // 0
    @SerialName("rFlatMagicPenetrationMod")
    val rFlatMagicPenetrationMod: Float? = null, // 0
    @SerialName("rFlatMagicPenetrationModPerLevel")
    val rFlatMagicPenetrationModPerLevel: Float? = null, // 0
    @SerialName("rFlatMovementSpeedModPerLevel")
    val rFlatMovementSpeedModPerLevel: Float? = null, // 0
    @SerialName("rFlatPhysicalDamageModPerLevel")
    val rFlatPhysicalDamageModPerLevel: Float? = null, // 0
    @SerialName("rFlatSpellBlockModPerLevel")
    val rFlatSpellBlockModPerLevel: Float? = null, // 0
    @SerialName("rFlatTimeDeadMod")
    val rFlatTimeDeadMod: Float? = null, // 0
    @SerialName("rFlatTimeDeadModPerLevel")
    val rFlatTimeDeadModPerLevel: Float? = null, // 0
    @SerialName("rPercentArmorPenetrationMod")
    val rPercentArmorPenetrationMod: Float? = null, // 0
    @SerialName("rPercentArmorPenetrationModPerLevel")
    val rPercentArmorPenetrationModPerLevel: Float? = null, // 0
    @SerialName("rPercentAttackSpeedModPerLevel")
    val rPercentAttackSpeedModPerLevel: Float? = null, // 0
    @SerialName("rPercentCooldownMod")
    val rPercentCooldownMod: Float? = null, // 0
    @SerialName("rPercentCooldownModPerLevel")
    val rPercentCooldownModPerLevel: Float? = null, // 0
    @SerialName("rPercentMagicPenetrationMod")
    val rPercentMagicPenetrationMod: Float? = null, // 0
    @SerialName("rPercentMagicPenetrationModPerLevel")
    val rPercentMagicPenetrationModPerLevel: Float? = null, // 0
    @SerialName("rPercentMovementSpeedModPerLevel")
    val rPercentMovementSpeedModPerLevel: Float? = null, // 0
    @SerialName("rPercentTimeDeadMod")
    val rPercentTimeDeadMod: Float? = null, // 0
    @SerialName("rPercentTimeDeadModPerLevel")
    val rPercentTimeDeadModPerLevel: Float? = null // 0
) : Parcelable

@Keep
@Serializable
@Parcelize
data class GameItemInfo(
    @SerialName("colloq")
    val colloq: String? = null, // ;똥신;boots;speed
    @SerialName("description")
    val description: String? = null, // <mainText><stats>이동 속도 <attention>25</attention></stats></mainText><br>
    @SerialName("gold")
    val gold: Gold? = null,
    @SerialName("image")
    val image: Image? = null,
    @SerialName("into")
    val into: List<String?>? = null,
    @SerialName("maps")
    val maps: Map<String, Boolean>? = null,
    @SerialName("name")
    val name: String? = null, // 장화
    @SerialName("plaintext")
    val plaintext: String? = null, // 이동 속도가 약간 증가합니다.
    @SerialName("stats")
    val stats: GameItemStats? = null,
    @SerialName("tags")
    val tags: List<String?>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Gold(
    @SerialName("base")
    val base: Int? = null, // 300
    @SerialName("purchasable")
    val purchasable: Boolean? = null, // true
    @SerialName("sell")
    val sell: Int? = null, // 210
    @SerialName("total")
    val total: Int? = null // 300
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Group(
    @SerialName("id")
    val id: String? = null, // HuntersTalismanGroup
    @SerialName("MaxGroupOwnable")
    val maxGroupOwnable: String? = null // 1
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Tree(
    @SerialName("header")
    val header: String? = null, // START
    @SerialName("tags")
    val tags: List<String?>? = null
) : Parcelable