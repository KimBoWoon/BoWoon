package com.domain.practice.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PokemonResponse(
    val count: Int? = null,
    val next: String? = null,
    val previous: String? = null,
    val results: MutableList<PokemonModel.Pokemon>? = null
) : Parcelable