package com.domain.rssReader.dto

data class Rss(
    val version: String? = null,
    val channel: Channel? = null
)

data class Channel(
    val title: String? = null,
    val link: String? = null,
    val description: String? = null,
    val language: String? = null,
    val copyright: String? = null,
    val generator: String? = null,
    val lastBuildDate: String? = null,
    val image: Image? = null,
    val items: List<Item>? = null,
    val pubDate: String? = null,
    val ttl: Int? = null
)

data class Image(
    val url: String? = null,
    val title: String? = null,
    val link: String? = null,
)

data class Item(
    val title: String? = null,
    val description: String? = null,
    val pubDate: String? = null,
    val link: String? = null,
    val guid: String? = null,
    val contentEncoded: String? = null,
    val dcCreator: String? = null,
    val mediaGroup: MediaGroup? = null,
    val mediaContent: MediaContent? = null,
    val category: Category? = null
)

data class MediaGroup(
    val mediaContent: List<MediaContent>? = null
)

data class MediaContent(
    val medium: String? = null,
    val url: String? = null,
    val height: Int? = null,
    val width: Int? = null,
    val type: String? = null,
    val expression: String? = null
)

data class Category(
    val domain: String? = null,
    val category: String? = null
)