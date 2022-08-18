package com.domain.practice.dto

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
@Keep
data class PokemonData(
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: List<SealedPokemon.Pokemon>? = null
) : Parcelable