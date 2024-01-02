package com.bowoon.rss_reader.data

import com.bowoon.rss_reader.util.encodeString

data class Article(
    val isHeader: Boolean = false,
    val feed: String? = null,
    private val title: String? = null,
    private val summary: String? = null,
    val linkUrl: String? = null
) {
    val encodeTitle: String?
        get() = title?.encodeString()
    val encodeSummary: String?
        get() = summary?.encodeString()
}