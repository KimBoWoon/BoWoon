package com.domain.practice.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

sealed class SealedPokemon {
    @Parcelize
    @Keep
    data class Pokemon(
        val name: String? = null,
        val url: String? = null
    ) : Parcelable, SealedPokemon() {
        fun getImageUrl(): String {
            val index = url?.split("/".toRegex())?.dropLast(1)?.last()
            return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
        }
    }

    @Keep
    data class PokemonHeader(
        val title: String? = null
    ) : SealedPokemon()

    @Keep
    data class PokemonFooter(
        val title: String? = null
    ) : SealedPokemon()
}