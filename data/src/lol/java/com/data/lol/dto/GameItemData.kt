package com.data.lol.dto

import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import kotlinx.serialization.Serializable

@Keep
@Serializable
@Parcelize
data class GameItemData(
    val basic: Basic? = null,
    val `data`: Map<String, GameItemInfo>? = null,
    val groups: List<Group?>? = null,
    val tree: List<Tree?>? = null,
    val type: String? = null, // item
    val version: String? = null // 12.14.1
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Basic(
    val colloq: String? = null, // ;
    val consumeOnFull: Boolean? = null, // false
    val consumed: Boolean? = null, // false
    val depth: Int? = null, // 1
    val description: String? = null,
    val from: List<String?>? = null,
    val gold: Gold? = null,
    val group: String? = null,
    val hideFromAll: Boolean? = null, // false
    val inStore: Boolean? = null, // true
    val into: List<String?>? = null,
    val maps: Map<String, Boolean>? = null,
    val name: String? = null,
    val plaintext: String? = null,
    val requiredAlly: String? = null,
    val requiredChampion: String? = null,
    val rune: Rune? = null,
    val specialRecipe: Int? = null, // 0
    val stacks: Int? = null, // 1
    val stats: GameItemStats? = null,
    val tags: List<String?>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Rune(
    val isrune: Boolean? = null, // false
    val tier: Int? = null, // 1
    val type: String? = null // red
) : Parcelable

@Keep
@Serializable
@Parcelize
data class GameItemStats(
    val flatArmorMod: Float? = null, // 0
    val flatAttackSpeedMod: Float? = null, // 0
    val flatBlockMod: Float? = null, // 0
    val flatCritChanceMod: Float? = null, // 0
    val flatCritDamageMod: Float? = null, // 0
    val flatEXPBonus: Float? = null, // 0
    val flatEnergyPoolMod: Float? = null, // 0
    val flatEnergyRegenMod: Float? = null, // 0
    val flatHPPoolMod: Float? = null, // 0
    val flatHPRegenMod: Float? = null, // 0
    val flatMPPoolMod: Float? = null, // 0
    val flatMPRegenMod: Float? = null, // 0
    val flatMagicDamageMod: Float? = null, // 0
    val flatMovementSpeedMod: Float? = null, // 0
    val flatPhysicalDamageMod: Float? = null, // 0
    val flatSpellBlockMod: Float? = null, // 0
    val percentArmorMod: Float? = null, // 0
    val percentAttackSpeedMod: Float? = null, // 0
    val percentBlockMod: Float? = null, // 0
    val percentCritChanceMod: Float? = null, // 0
    val percentCritDamageMod: Float? = null, // 0
    val percentDodgeMod: Float? = null, // 0
    val percentEXPBonus: Float? = null, // 0
    val percentHPPoolMod: Float? = null, // 0
    val percentHPRegenMod: Float? = null, // 0
    val percentLifeStealMod: Float? = null, // 0
    val percentMPPoolMod: Float? = null, // 0
    val percentMPRegenMod: Float? = null, // 0
    val percentMagicDamageMod: Float? = null, // 0
    val percentMovementSpeedMod: Float? = null, // 0
    val percentPhysicalDamageMod: Float? = null, // 0
    val percentSpellBlockMod: Float? = null, // 0
    val percentSpellVampMod: Float? = null, // 0
    val rFlatArmorModPerLevel: Float? = null, // 0
    val rFlatArmorPenetrationMod: Float? = null, // 0
    val rFlatArmorPenetrationModPerLevel: Float? = null, // 0
    val rFlatCritChanceModPerLevel: Float? = null, // 0
    val rFlatCritDamageModPerLevel: Float? = null, // 0
    val rFlatDodgeMod: Float? = null, // 0
    val rFlatDodgeModPerLevel: Float? = null, // 0
    val rFlatEnergyModPerLevel: Float? = null, // 0
    val rFlatEnergyRegenModPerLevel: Float? = null, // 0
    val rFlatGoldPer10Mod: Float? = null, // 0
    val rFlatHPModPerLevel: Float? = null, // 0
    val rFlatHPRegenModPerLevel: Float? = null, // 0
    val rFlatMPModPerLevel: Float? = null, // 0
    val rFlatMPRegenModPerLevel: Float? = null, // 0
    val rFlatMagicDamageModPerLevel: Float? = null, // 0
    val rFlatMagicPenetrationMod: Float? = null, // 0
    val rFlatMagicPenetrationModPerLevel: Float? = null, // 0
    val rFlatMovementSpeedModPerLevel: Float? = null, // 0
    val rFlatPhysicalDamageModPerLevel: Float? = null, // 0
    val rFlatSpellBlockModPerLevel: Float? = null, // 0
    val rFlatTimeDeadMod: Float? = null, // 0
    val rFlatTimeDeadModPerLevel: Float? = null, // 0
    val rPercentArmorPenetrationMod: Float? = null, // 0
    val rPercentArmorPenetrationModPerLevel: Float? = null, // 0
    val rPercentAttackSpeedModPerLevel: Float? = null, // 0
    val rPercentCooldownMod: Float? = null, // 0
    val rPercentCooldownModPerLevel: Float? = null, // 0
    val rPercentMagicPenetrationMod: Float? = null, // 0
    val rPercentMagicPenetrationModPerLevel: Float? = null, // 0
    val rPercentMovementSpeedModPerLevel: Float? = null, // 0
    val rPercentTimeDeadMod: Float? = null, // 0
    val rPercentTimeDeadModPerLevel: Float? = null // 0
) : Parcelable

@Keep
@Serializable
@Parcelize
data class GameItemInfo(
    val colloq: String? = null, // ;똥신;boots;speed
    val description: String? = null, // <mainText><stats>이동 속도 <attention>25</attention></stats></mainText><br>
    val gold: Gold? = null,
    val image: Image? = null,
    val into: List<String?>? = null,
    val inStore: Boolean? = null, // true
    val consumed: Boolean? = null,
    val stacks: Int? = null,
    val hideFromAll: Boolean? = null,
    val maps: Map<String, Boolean>? = null,
    val name: String? = null, // 장화
    val plaintext: String? = null, // 이동 속도가 약간 증가합니다.
    val stats: GameItemStats? = null,
    val tags: List<String?>? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Gold(
    val base: Int? = null, // 300
    val purchasable: Boolean? = null, // true
    val sell: Int? = null, // 210
    val total: Int? = null // 300
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Group(
    val id: String? = null, // HuntersTalismanGroup
    val maxGroupOwnable: String? = null // 1
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Tree(
    val header: String? = null, // START
    val tags: List<String?>? = null
) : Parcelable