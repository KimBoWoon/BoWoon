package com.domain.practice.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

sealed class PokemonModel {
    @Parcelize
    @Serializable
    data class Pokemon(
        val name: String? = null,
        val url: String? = null
    ) : Parcelable, PokemonModel() {
        fun getImageUrl(): String {
            val index = url?.split("/".toRegex())?.dropLast(1)?.last()
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
        }
    }

    data class PokemonHeader(
        val title: String? = null
    ) : PokemonModel()

    data class PokemonFooter(
        val title: String? = null
    ) : PokemonModel()
}