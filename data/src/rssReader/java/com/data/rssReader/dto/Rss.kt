package com.data.rssReader.dto

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "rss")
data class Rss(
    @Attribute(name = "version")
    val version: String? = null,
    @Element(name = "channel")
    val channel: Channel? = null
)

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
)

@Xml(name = "image")
data class Image(
    @PropertyElement(name = "url")
    val url: String? = null,
    @PropertyElement(name = "title")
    val title: String? = null,
    @PropertyElement(name = "link")
    val link: String? = null,
)

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
)

@Xml(name = "media:group")
data class MediaGroup(
    @Element(name = "media:content")
    val mediaContent: List<MediaContent>? = null
)

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
)

@Xml(name = "category")
data class Category(
    @Attribute(name = "domain")
    val domain: String? = null,
    @PropertyElement(name = "category")
    val category: String? = null
)