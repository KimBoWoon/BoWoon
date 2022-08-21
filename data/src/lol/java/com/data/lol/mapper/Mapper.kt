package com.data.lol.mapper

import com.data.lol.dto.*
import com.data.lol.dto.Basic
import com.data.lol.dto.GameItemStats
import com.data.lol.dto.Gold
import com.data.lol.dto.Group
import com.data.lol.dto.Rune
import com.data.lol.dto.Tree
import com.domain.lol.dto.*
import com.domain.lol.dto.ChampionDetail
import com.domain.lol.dto.ChampionStats
import com.domain.lol.dto.Datavalues
import com.domain.lol.dto.GameItemInfo
import com.domain.lol.dto.Image
import com.domain.lol.dto.Info
import com.domain.lol.dto.Leveltip
import com.domain.lol.dto.Passive
import com.domain.lol.dto.Skin
import com.domain.lol.dto.Spell

fun ChampionData.convert(): ChampionRoot? {
    data?.keys?.let { keys ->
        mutableMapOf<String, ChampionInfo>().apply {
            keys.forEach { key ->
                val championInfo = ChampionInfo(
                    data[key]?.blurb,
                    data[key]?.id,
                    data[key]?.image?.convert(),
                    data[key]?.info?.convert(),
                    data[key]?.key,
                    data[key]?.name,
                    data[key]?.partype,
                    data[key]?.stats?.convert(),
                    data[key]?.tags,
                    data[key]?.title,
                    data[key]?.version
                )
                this[key] = championInfo
            }
            return ChampionRoot(type, format, version, this)
        }
    }
    return null
}

fun Champion.convert(): ChampionInfo =
    ChampionInfo(
        blurb,
        id,
        image?.convert(),
        info?.convert(),
        key,
        name,
        partype,
        stats?.convert(),
        tags,
        title,
        version
    )

fun com.data.lol.dto.Image.convert(): Image = Image(full, group, sprite, h, w, x, y)

fun com.data.lol.dto.Info.convert(): Info = Info(attack, defense, difficulty, magic)

fun com.data.lol.dto.ChampionStats.convert(): ChampionStats =
    ChampionStats(
        armor,
        armorperlevel,
        attackdamage,
        attackdamageperlevel,
        attackrange,
        attackspeed,
        attackspeedperlevel,
        crit,
        critperlevel,
        hp,
        hpperlevel,
        hpregen,
        hpregenperlevel,
        movespeed,
        mp,
        mpperlevel,
        mpregen,
        mpregenperlevel,
        spellblock,
        spellblockperlevel
    )

fun ChampionDetailData.convert(): ChampionDetailRoot? {
    data?.keys?.let { keys ->
        mutableMapOf<String, ChampionDetail>().apply {
            keys.forEach { key ->
                this[key] = ChampionDetail(
                    data[key]?.allytips,
                    data[key]?.blurb,
                    data[key]?.enemytips,
                    data[key]?.id,
                    data[key]?.image?.convert(),
                    data[key]?.info?.convert(),
                    data[key]?.key,
                    data[key]?.lore,
                    data[key]?.name,
                    data[key]?.partype,
                    data[key]?.passive?.convert(),
                    data[key]?.skins?.map { it?.convert() },
                    data[key]?.spells?.map { it?.convert() },
                    data[key]?.stats?.convert(),
                    data[key]?.tags,
                    data[key]?.title
                )
            }
            return ChampionDetailRoot(this, format, type, version)
        }
    }
    return null
}

fun com.data.lol.dto.Passive.convert(): Passive =
    Passive(description, image?.convert(), name)

fun com.data.lol.dto.Skin.convert(): Skin =
    Skin(chromas, id, name, num)

fun com.data.lol.dto.Spell.convert(): Spell =
    Spell(
        cooldown,
        cooldownBurn,
        cost,
        costBurn,
        costType,
        datavalues?.convert(),
        description,
        effect,
        effectBurn,
        id,
        image?.convert(),
        leveltip?.convert(),
        maxammo,
        maxrank,
        name,
        range,
        rangeBurn,
        resource,
        tooltip
    )

fun com.data.lol.dto.Datavalues.convert(): Datavalues =
    Datavalues()

fun com.data.lol.dto.Leveltip.convert(): Leveltip =
    Leveltip(effect, label)

fun GameItemData.convert(): GameItemRoot? {
    data?.keys?.let { keys ->
        mutableMapOf<String, GameItemInfo>().apply {
            keys.forEach { key ->
                this[key] = GameItemInfo(
                    data[key]?.colloq,
                    data[key]?.description,
                    data[key]?.gold?.convert(),
                    data[key]?.image?.convert(),
                    data[key]?.into,
                    data[key]?.maps,
                    data[key]?.name,
                    data[key]?.plaintext,
                    data[key]?.stats?.convert(),
                    data[key]?.tags
                )
            }
            return GameItemRoot(
                basic?.convert(),
                this,
                groups?.map { it?.convert() },
                tree?.map { it?.convert() },
                type,
                version
            )
        }
    }
    return null
}

fun Basic.convert(): com.domain.lol.dto.Basic =
    com.domain.lol.dto.Basic(
        colloq,
        consumeOnFull,
        consumed,
        depth,
        description,
        from,
        gold?.convert(),
        group,
        hideFromAll,
        inStore,
        into,
        maps,
        name,
        plaintext,
        requiredAlly,
        requiredChampion,
        rune?.convert(),
        specialRecipe,
        stacks,
        stats?.convert(),
        tags
    )

fun Gold.convert(): com.domain.lol.dto.Gold =
    com.domain.lol.dto.Gold(base, purchasable, sell, total)

fun Rune.convert(): com.domain.lol.dto.Rune =
    com.domain.lol.dto.Rune(isrune, tier, type)

fun GameItemStats.convert(): com.domain.lol.dto.GameItemStats =
    com.domain.lol.dto.GameItemStats(
        flatArmorMod,
        flatAttackSpeedMod,
        flatBlockMod,
        flatCritChanceMod,
        flatCritDamageMod,
        flatEXPBonus,
        flatEnergyPoolMod,
        flatEnergyRegenMod,
        flatHPPoolMod,
        flatHPRegenMod,
        flatMPPoolMod,
        flatMPRegenMod,
        flatMagicDamageMod,
        flatMovementSpeedMod,
        flatPhysicalDamageMod,
        flatSpellBlockMod,
        percentArmorMod,
        percentAttackSpeedMod,
        percentBlockMod,
        percentCritChanceMod,
        percentCritDamageMod,
        percentDodgeMod,
        percentEXPBonus,
        percentHPPoolMod,
        percentHPRegenMod,
        percentLifeStealMod,
        percentMPPoolMod,
        percentMPRegenMod,
        percentMagicDamageMod,
        percentMovementSpeedMod,
        percentPhysicalDamageMod,
        percentSpellBlockMod,
        percentSpellVampMod,
        rFlatArmorModPerLevel,
        rFlatArmorPenetrationMod,
        rFlatArmorPenetrationModPerLevel,
        rFlatCritChanceModPerLevel,
        rFlatCritDamageModPerLevel,
        rFlatDodgeMod,
        rFlatDodgeModPerLevel,
        rFlatEnergyModPerLevel,
        rFlatEnergyRegenModPerLevel,
        rFlatGoldPer10Mod,
        rFlatHPModPerLevel,
        rFlatHPRegenModPerLevel,
        rFlatMPModPerLevel,
        rFlatMPRegenModPerLevel,
        rFlatMagicDamageModPerLevel,
        rFlatMagicPenetrationMod,
        rFlatMagicPenetrationModPerLevel,
        rFlatMovementSpeedModPerLevel,
        rFlatPhysicalDamageModPerLevel,
        rFlatSpellBlockModPerLevel,
        rFlatTimeDeadMod,
        rFlatTimeDeadModPerLevel,
        rPercentArmorPenetrationMod,
        rPercentArmorPenetrationModPerLevel,
        rPercentAttackSpeedModPerLevel,
        rPercentCooldownMod,
        rPercentCooldownModPerLevel,
        rPercentMagicPenetrationMod,
        rPercentMagicPenetrationModPerLevel,
        rPercentMovementSpeedModPerLevel,
        rPercentTimeDeadMod,
        rPercentTimeDeadModPerLevel
    )

fun Group.convert(): com.domain.lol.dto.Group =
    com.domain.lol.dto.Group(id, maxGroupOwnable)

fun Tree.convert(): com.domain.lol.dto.Tree =
    com.domain.lol.dto.Tree(header, tags)