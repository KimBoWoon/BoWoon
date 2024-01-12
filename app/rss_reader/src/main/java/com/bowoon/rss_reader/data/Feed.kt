package com.bowoon.rss_reader.data

import kotlinx.serialization.Serializable

@Serializable
data class Feed(
    val name: String? = null,
    val url: String? = null
)