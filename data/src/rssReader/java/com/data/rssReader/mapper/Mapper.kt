package com.data.rssReader.mapper

import com.data.rssReader.dto.Category
import com.data.rssReader.dto.Channel
import com.data.rssReader.dto.Image
import com.data.rssReader.dto.Item
import com.data.rssReader.dto.MediaContent
import com.data.rssReader.dto.MediaGroup
import com.data.rssReader.dto.Rss

fun Rss.nprRssDataMapper(): com.domain.rssReader.dto.Rss =
    com.domain.rssReader.dto.Rss(version, channel?.channelMapper())

fun Channel.channelMapper(): com.domain.rssReader.dto.Channel =
    com.domain.rssReader.dto.Channel(
        title,
        link,
        description,
        language,
        copyright,
        generator,
        lastBuildDate,
        image?.imageMapper(),
        items?.itemMapper(),
        pubDate,
        ttl
    )

fun Image.imageMapper(): com.domain.rssReader.dto.Image =
    com.domain.rssReader.dto.Image(url, title, link)

fun List<Item>.itemMapper(): List<com.domain.rssReader.dto.Item> =
    map {
        com.domain.rssReader.dto.Item(
            it.title,
            it.description,
            it.pubDate,
            it.link,
            it.guid,
            it.contentEncoded,
            it.dcCreator,
            it.mediaGroup?.mapper(),
            it.mediaContent?.mapper(),
            it.category?.mapper()
        )
    }

fun MediaGroup.mapper(): com.domain.rssReader.dto.MediaGroup =
    com.domain.rssReader.dto.MediaGroup(mediaContent?.mapper())

fun List<MediaContent>.mapper(): List<com.domain.rssReader.dto.MediaContent> =
    map {
        com.domain.rssReader.dto.MediaContent(
            it.medium,
            it.url,
            it.height,
            it.width,
            it.type,
            it.expression
        )
    }

fun MediaContent.mapper(): com.domain.rssReader.dto.MediaContent =
    com.domain.rssReader.dto.MediaContent(
        medium,
        url,
        height,
        width,
        type,
        expression
    )

fun Category.mapper(): com.domain.rssReader.dto.Category =
    com.domain.rssReader.dto.Category(domain, category)