package com.rss_reader.dto

data class Article(
    val isHeader: Boolean = false,
    val feed: String? = null,
    private val title: String? = null,
    private val summary: String? = null,
    val linkUrl: String? = null
) {
    val encodeTitle: String?
        get() = title?.replace(" ", "\u00A0")
            ?.replace("&lt;", "<")
            ?.replace("&gt;", ">")
            ?.replace("&amp;", "&")
            ?.replace("&apos;", "'")
            ?.replace("&quot;", "\"")
            ?.replace("&middot;", "．")
    val encodeSummary: String?
        get() = summary?.replace(" ", "\u00A0")
            ?.replace("&lt;", "<")
            ?.replace("&gt;", ">")
            ?.replace("&amp;", "&")
            ?.replace("&apos;", "'")
            ?.replace("&quot;", "\"")
            ?.replace("&middot;", "．")
}