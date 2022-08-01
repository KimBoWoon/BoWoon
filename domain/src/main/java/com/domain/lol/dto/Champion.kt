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
    val `data`: Data? = null
) : Parcelable

@Keep
@Serializable
@Parcelize
data class Data(
    @SerialName("Aatrox")
    val aatrox: ChampionInfo? = null,
    @SerialName("Ahri")
    val ahri: ChampionInfo? = null,
    @SerialName("Akali")
    val akali: ChampionInfo? = null,
    @SerialName("Akshan")
    val akshan: ChampionInfo? = null,
    @SerialName("Alistar")
    val alistar: ChampionInfo? = null,
    @SerialName("Amumu")
    val amumu: ChampionInfo? = null,
    @SerialName("Anivia")
    val anivia: ChampionInfo? = null,
    @SerialName("Annie")
    val annie: ChampionInfo? = null,
    @SerialName("Aphelios")
    val aphelios: ChampionInfo? = null,
    @SerialName("Ashe")
    val ashe: ChampionInfo? = null,
    @SerialName("AurelionSol")
    val aurelionSol: ChampionInfo? = null,
    @SerialName("Azir")
    val azir: ChampionInfo? = null,
    @SerialName("Bard")
    val bard: ChampionInfo? = null,
    @SerialName("Belveth")
    val belveth: ChampionInfo? = null,
    @SerialName("Blitzcrank")
    val blitzcrank: ChampionInfo? = null,
    @SerialName("Brand")
    val brand: ChampionInfo? = null,
    @SerialName("Braum")
    val braum: ChampionInfo? = null,
    @SerialName("Caitlyn")
    val caitlyn: ChampionInfo? = null,
    @SerialName("Camille")
    val camille: ChampionInfo? = null,
    @SerialName("Cassiopeia")
    val cassiopeia: ChampionInfo? = null,
    @SerialName("Chogath")
    val chogath: ChampionInfo? = null,
    @SerialName("Corki")
    val corki: ChampionInfo? = null,
    @SerialName("Darius")
    val darius: ChampionInfo? = null,
    @SerialName("Diana")
    val diana: ChampionInfo? = null,
    @SerialName("DrMundo")
    val drMundo: ChampionInfo? = null,
    @SerialName("Draven")
    val draven: ChampionInfo? = null,
    @SerialName("Ekko")
    val ekko: ChampionInfo? = null,
    @SerialName("Elise")
    val elise: ChampionInfo? = null,
    @SerialName("Evelynn")
    val evelynn: ChampionInfo? = null,
    @SerialName("Ezreal")
    val ezreal: ChampionInfo? = null,
    @SerialName("Fiddlesticks")
    val fiddlesticks: ChampionInfo? = null,
    @SerialName("Fiora")
    val fiora: ChampionInfo? = null,
    @SerialName("Fizz")
    val fizz: ChampionInfo? = null,
    @SerialName("Galio")
    val galio: ChampionInfo? = null,
    @SerialName("Gangplank")
    val gangplank: ChampionInfo? = null,
    @SerialName("Garen")
    val garen: ChampionInfo? = null,
    @SerialName("Gnar")
    val gnar: ChampionInfo? = null,
    @SerialName("Gragas")
    val gragas: ChampionInfo? = null,
    @SerialName("Graves")
    val graves: ChampionInfo? = null,
    @SerialName("Gwen")
    val gwen: ChampionInfo? = null,
    @SerialName("Hecarim")
    val hecarim: ChampionInfo? = null,
    @SerialName("Heimerdinger")
    val heimerdinger: ChampionInfo? = null,
    @SerialName("Illaoi")
    val illaoi: ChampionInfo? = null,
    @SerialName("Irelia")
    val irelia: ChampionInfo? = null,
    @SerialName("Ivern")
    val ivern: ChampionInfo? = null,
    @SerialName("Janna")
    val janna: ChampionInfo? = null,
    @SerialName("JarvanIV")
    val jarvanIV: ChampionInfo? = null,
    @SerialName("Jax")
    val jax: ChampionInfo? = null,
    @SerialName("Jayce")
    val jayce: ChampionInfo? = null,
    @SerialName("Jhin")
    val jhin: ChampionInfo? = null,
    @SerialName("Jinx")
    val jinx: ChampionInfo? = null,
    @SerialName("Kaisa")
    val kaisa: ChampionInfo? = null,
    @SerialName("Kalista")
    val kalista: ChampionInfo? = null,
    @SerialName("Karma")
    val karma: ChampionInfo? = null,
    @SerialName("Karthus")
    val karthus: ChampionInfo? = null,
    @SerialName("Kassadin")
    val kassadin: ChampionInfo? = null,
    @SerialName("Katarina")
    val katarina: ChampionInfo? = null,
    @SerialName("Kayle")
    val kayle: ChampionInfo? = null,
    @SerialName("Kayn")
    val kayn: ChampionInfo? = null,
    @SerialName("Kennen")
    val kennen: ChampionInfo? = null,
    @SerialName("Khazix")
    val khazix: ChampionInfo? = null,
    @SerialName("Kindred")
    val kindred: ChampionInfo? = null,
    @SerialName("Kled")
    val kled: ChampionInfo? = null,
    @SerialName("KogMaw")
    val kogMaw: ChampionInfo? = null,
    @SerialName("Leblanc")
    val leblanc: ChampionInfo? = null,
    @SerialName("LeeSin")
    val leeSin: ChampionInfo? = null,
    @SerialName("Leona")
    val leona: ChampionInfo? = null,
    @SerialName("Lillia")
    val lillia: ChampionInfo? = null,
    @SerialName("Lissandra")
    val lissandra: ChampionInfo? = null,
    @SerialName("Lucian")
    val lucian: ChampionInfo? = null,
    @SerialName("Lulu")
    val lulu: ChampionInfo? = null,
    @SerialName("Lux")
    val lux: ChampionInfo? = null,
    @SerialName("Malphite")
    val malphite: ChampionInfo? = null,
    @SerialName("Malzahar")
    val malzahar: ChampionInfo? = null,
    @SerialName("Maokai")
    val maokai: ChampionInfo? = null,
    @SerialName("MasterYi")
    val masterYi: ChampionInfo? = null,
    @SerialName("MissFortune")
    val missFortune: ChampionInfo? = null,
    @SerialName("MonkeyKing")
    val monkeyKing: ChampionInfo? = null,
    @SerialName("Mordekaiser")
    val mordekaiser: ChampionInfo? = null,
    @SerialName("Morgana")
    val morgana: ChampionInfo? = null,
    @SerialName("Nami")
    val nami: ChampionInfo? = null,
    @SerialName("Nasus")
    val nasus: ChampionInfo? = null,
    @SerialName("Nautilus")
    val nautilus: ChampionInfo? = null,
    @SerialName("Neeko")
    val neeko: ChampionInfo? = null,
    @SerialName("Nidalee")
    val nidalee: ChampionInfo? = null,
    @SerialName("Nilah")
    val nilah: ChampionInfo? = null,
    @SerialName("Nocturne")
    val nocturne: ChampionInfo? = null,
    @SerialName("Nunu")
    val nunu: ChampionInfo? = null,
    @SerialName("Olaf")
    val olaf: ChampionInfo? = null,
    @SerialName("Orianna")
    val orianna: ChampionInfo? = null,
    @SerialName("Ornn")
    val ornn: ChampionInfo? = null,
    @SerialName("Pantheon")
    val pantheon: ChampionInfo? = null,
    @SerialName("Poppy")
    val poppy: ChampionInfo? = null,
    @SerialName("Pyke")
    val pyke: ChampionInfo? = null,
    @SerialName("Qiyana")
    val qiyana: ChampionInfo? = null,
    @SerialName("Quinn")
    val quinn: ChampionInfo? = null,
    @SerialName("Rakan")
    val rakan: ChampionInfo? = null,
    @SerialName("Rammus")
    val rammus: ChampionInfo? = null,
    @SerialName("RekSai")
    val rekSai: ChampionInfo? = null,
    @SerialName("Rell")
    val rell: ChampionInfo? = null,
    @SerialName("Renata")
    val renata: ChampionInfo? = null,
    @SerialName("Renekton")
    val renekton: ChampionInfo? = null,
    @SerialName("Rengar")
    val rengar: ChampionInfo? = null,
    @SerialName("Riven")
    val riven: ChampionInfo? = null,
    @SerialName("Rumble")
    val rumble: ChampionInfo? = null,
    @SerialName("Ryze")
    val ryze: ChampionInfo? = null,
    @SerialName("Samira")
    val samira: ChampionInfo? = null,
    @SerialName("Sejuani")
    val sejuani: ChampionInfo? = null,
    @SerialName("Senna")
    val senna: ChampionInfo? = null,
    @SerialName("Seraphine")
    val seraphine: ChampionInfo? = null,
    @SerialName("Sett")
    val sett: ChampionInfo? = null,
    @SerialName("Shaco")
    val shaco: ChampionInfo? = null,
    @SerialName("Shen")
    val shen: ChampionInfo? = null,
    @SerialName("Shyvana")
    val shyvana: ChampionInfo? = null,
    @SerialName("Singed")
    val singed: ChampionInfo? = null,
    @SerialName("Sion")
    val sion: ChampionInfo? = null,
    @SerialName("Sivir")
    val sivir: ChampionInfo? = null,
    @SerialName("Skarner")
    val skarner: ChampionInfo? = null,
    @SerialName("Sona")
    val sona: ChampionInfo? = null,
    @SerialName("Soraka")
    val soraka: ChampionInfo? = null,
    @SerialName("Swain")
    val swain: ChampionInfo? = null,
    @SerialName("Sylas")
    val sylas: ChampionInfo? = null,
    @SerialName("Syndra")
    val syndra: ChampionInfo? = null,
    @SerialName("TahmKench")
    val tahmKench: ChampionInfo? = null,
    @SerialName("Taliyah")
    val taliyah: ChampionInfo? = null,
    @SerialName("Talon")
    val talon: ChampionInfo? = null,
    @SerialName("Taric")
    val taric: ChampionInfo? = null,
    @SerialName("Teemo")
    val teemo: ChampionInfo? = null,
    @SerialName("Thresh")
    val thresh: ChampionInfo? = null,
    @SerialName("Tristana")
    val tristana: ChampionInfo? = null,
    @SerialName("Trundle")
    val trundle: ChampionInfo? = null,
    @SerialName("Tryndamere")
    val tryndamere: ChampionInfo? = null,
    @SerialName("TwistedFate")
    val twistedFate: ChampionInfo? = null,
    @SerialName("Twitch")
    val twitch: ChampionInfo? = null,
    @SerialName("Udyr")
    val udyr: ChampionInfo? = null,
    @SerialName("Urgot")
    val urgot: ChampionInfo? = null,
    @SerialName("Varus")
    val varus: ChampionInfo? = null,
    @SerialName("Vayne")
    val vayne: ChampionInfo? = null,
    @SerialName("Veigar")
    val veigar: ChampionInfo? = null,
    @SerialName("Velkoz")
    val velkoz: ChampionInfo? = null,
    @SerialName("Vex")
    val vex: ChampionInfo? = null,
    @SerialName("Vi")
    val vi: ChampionInfo? = null,
    @SerialName("Viego")
    val viego: ChampionInfo? = null,
    @SerialName("Viktor")
    val viktor: ChampionInfo? = null,
    @SerialName("Vladimir")
    val vladimir: ChampionInfo? = null,
    @SerialName("Volibear")
    val volibear: ChampionInfo? = null,
    @SerialName("Warwick")
    val warwick: ChampionInfo? = null,
    @SerialName("Xayah")
    val xayah: ChampionInfo? = null,
    @SerialName("Xerath")
    val xerath: ChampionInfo? = null,
    @SerialName("XinZhao")
    val xinZhao: ChampionInfo? = null,
    @SerialName("Yasuo")
    val yasuo: ChampionInfo? = null,
    @SerialName("Yone")
    val yone: ChampionInfo? = null,
    @SerialName("Yorick")
    val yorick: ChampionInfo? = null,
    @SerialName("Yuumi")
    val yuumi: ChampionInfo? = null,
    @SerialName("Zac")
    val zac: ChampionInfo? = null,
    @SerialName("Zed")
    val zed: ChampionInfo? = null,
    @SerialName("Zeri")
    val zeri: ChampionInfo? = null,
    @SerialName("Ziggs")
    val ziggs: ChampionInfo? = null,
    @SerialName("Zilean")
    val zilean: ChampionInfo? = null,
    @SerialName("Zoe")
    val zoe: ChampionInfo? = null,
    @SerialName("Zyra")
    val zyra: ChampionInfo? = null
) : Parcelable {
    fun getChampionList(): List<ChampionInfo?> = listOf(
        aatrox,
        ahri,
        akali,
        akshan,
        alistar,
        amumu,
        anivia,
        annie,
        aphelios,
        ashe,
        aurelionSol,
        azir,
        bard,
        belveth,
        blitzcrank,
        brand,
        braum,
        caitlyn,
        camille,
        cassiopeia,
        chogath,
        corki,
        darius,
        diana,
        drMundo,
        draven,
        ekko,
        elise,
        evelynn,
        ezreal,
        fiddlesticks,
        fiora,
        fizz,
        galio,
        gangplank,
        garen,
        gnar,
        gragas,
        graves,
        gwen,
        hecarim,
        heimerdinger,
        illaoi,
        irelia,
        ivern,
        janna,
        jarvanIV,
        jax,
        jayce,
        jhin,
        jinx,
        kaisa,
        kalista,
        karma,
        karthus,
        kassadin,
        katarina,
        kayle,
        kayn,
        kennen,
        khazix,
        kindred,
        kled,
        kogMaw,
        leblanc,
        leeSin,
        leona,
        lillia,
        lissandra,
        lucian,
        lulu,
        lux,
        malphite,
        malzahar,
        maokai,
        masterYi,
        missFortune,
        monkeyKing,
        mordekaiser,
        morgana,
        nami,
        nasus,
        nautilus,
        neeko,
        nidalee,
        nilah,
        nocturne,
        nunu,
        olaf,
        orianna,
        ornn,
        pantheon,
        poppy,
        pyke,
        qiyana,
        quinn,
        rakan,
        rammus,
        rekSai,
        rell,
        renata,
        renekton,
        rengar,
        riven,
        rumble,
        ryze,
        samira,
        sejuani,
        senna,
        seraphine,
        sett,
        shaco,
        shen,
        shyvana,
        singed,
        sion,
        sivir,
        skarner,
        sona,
        soraka,
        swain,
        sylas,
        syndra,
        tahmKench,
        taliyah,
        talon,
        taric,
        teemo,
        thresh,
        tristana,
        trundle,
        tryndamere,
        twistedFate,
        twitch,
        udyr,
        urgot,
        varus,
        vayne,
        veigar,
        velkoz,
        vex,
        vi,
        viego,
        viktor,
        vladimir,
        volibear,
        warwick,
        xayah,
        xerath,
        xinZhao,
        yasuo,
        yone,
        yorick,
        yuumi,
        zac,
        zed,
        zeri,
        ziggs,
        zilean,
        zoe,
        zyra
    )
}

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
    fun getChampionSquareImage(version: String? = null): String =
        "https://ddragon.leagueoflegends.com/cdn/$version/img/champion/$full"
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
data class Stats(
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