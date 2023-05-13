package com.rss_reader.dto

data class Article(
    val isHeader: Boolean = false,
    val feed: String? = null,
    val title: String? = null,
    val summary: String? = null
)