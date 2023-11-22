package com.data.rssReader.dto

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

interface Mapper<DOMAIN> {
    fun mapper(): DOMAIN
}

@Xml(name = "rss")
data class Rss(
    @Attribute(name = "version")
    val version: String? = null,
    @Element(name = "channel")
    val channel: Channel? = null
) : Mapper<com.domain.rssReader.dto.Rss> {
    override fun mapper(): com.domain.rssReader.dto.Rss =
        com.domain.rssReader.dto.Rss(version, channel?.mapper())
}

@Xml(name = "channel")
data class Channel(
    @PropertyElement(name = "title")
    val title: String? = null,
    @PropertyElement(name = "link")
    val link: String? = null,
    @PropertyElement(name = "description")
    val description: String? = null,
    @PropertyElement(name = "language")
    val language: String? = null,
    @PropertyElement(name = "copyright")
    val copyright: String? = null,
    @PropertyElement(name = "generator")
    val generator: String? = null,
    @PropertyElement(name = "lastBuildDate")
    val lastBuildDate: String? = null,
    @Element(name = "image")
    val image: Image? = null,
    @Element(name = "item")
    val items: List<Item>? = null,
    @PropertyElement(name = "pubDate")
    val pubDate: String? = null,
    @PropertyElement(name = "ttl")
    val ttl: Int? = null
) : Mapper<com.domain.rssReader.dto.Channel> {
    override fun mapper(): com.domain.rssReader.dto.Channel =
        com.domain.rssReader.dto.Channel(
            title,
            link,
            description,
            language,
            copyright,
            generator,
            lastBuildDate,
            image?.mapper(),
            items?.listMapper(),
            pubDate,
            ttl
        )
}

@Xml(name = "image")
data class Image(
    @PropertyElement(name = "url")
    val url: String? = null,
    @PropertyElement(name = "title")
    val title: String? = null,
    @PropertyElement(name = "link")
    val link: String? = null,
) : Mapper<com.domain.rssReader.dto.Image> {
    override fun mapper(): com.domain.rssReader.dto.Image =
        com.domain.rssReader.dto.Image(url, title, link)
}

@Xml(name = "item")
data class Item(
    @PropertyElement(name = "title")
    val title: String? = null,
    @PropertyElement(name = "description")
    val description: String? = null,
    @PropertyElement(name = "pubDate")
    val pubDate: String? = null,
    @PropertyElement(name = "link")
    val link: String? = null,
    @PropertyElement(name = "guid")
    val guid: String? = null,
    @PropertyElement(name = "content:encoded")
    val contentEncoded: String? = null,
    @PropertyElement(name = "dc:creator")
    val dcCreator: String? = null,
    @Element(name = "media:group")
    val mediaGroup: MediaGroup? = null,
    @Element(name = "media:content")
    val mediaContent: MediaContent? = null,
    @Element(name = "category")
    val category: Category? = null
) : Mapper<com.domain.rssReader.dto.Item> {
    override fun mapper(): com.domain.rssReader.dto.Item =
        com.domain.rssReader.dto.Item(
            title,
            description,
            pubDate,
            link,
            guid,
            contentEncoded,
            dcCreator,
            mediaGroup?.mapper(),
            mediaContent?.mapper(),
            category?.mapper()
        )
}

@Xml(name = "media:group")
data class MediaGroup(
    @Element(name = "media:content")
    val mediaContent: List<MediaContent>? = null
) : Mapper<com.domain.rssReader.dto.MediaGroup> {
    override fun mapper(): com.domain.rssReader.dto.MediaGroup =
        com.domain.rssReader.dto.MediaGroup(mediaContent?.listMapper())
}

@Xml(name = "media:content")
data class MediaContent(
    @Attribute(name = "medium")
    val medium: String? = null,
    @Attribute(name = "url")
    val url: String? = null,
    @Attribute(name = "height")
    val height: Int? = null,
    @Attribute(name = "width")
    val width: Int? = null,
    @Attribute(name = "type")
    val type: String? = null,
    @Attribute(name = "expression")
    val expression: String? = null
) : Mapper<com.domain.rssReader.dto.MediaContent> {
    override fun mapper(): com.domain.rssReader.dto.MediaContent =
        com.domain.rssReader.dto.MediaContent(
            medium,
            url,
            height,
            width,
            type,
            expression
        )
}

@Xml(name = "category")
data class Category(
    @Attribute(name = "domain")
    val domain: String? = null,
    @PropertyElement(name = "category")
    val category: String? = null
) : Mapper<com.domain.rssReader.dto.Category> {
    override fun mapper(): com.domain.rssReader.dto.Category =
        com.domain.rssReader.dto.Category(domain, category)
}

@JvmName(name = "itemListMapper")
fun List<Item>.listMapper(): List<com.domain.rssReader.dto.Item> =
    map { it.mapper() }

@JvmName(name = "mediaContentListMapper")
fun List<MediaContent>.listMapper(): List<com.domain.rssReader.dto.MediaContent> =
    map { it.mapper() }