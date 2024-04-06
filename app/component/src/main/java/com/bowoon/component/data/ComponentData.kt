package com.bowoon.component.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComponentData(
    @SerialName("components")
    val components: List<Component?>? = null
)

@Serializable
data class Component(
    @SerialName("content")
    val content: Content? = null,
    @SerialName("type")
    val type: String? = null
)

@Serializable
data class Content(
    @SerialName("width")
    val width: Int? = null,
    @SerialName("height")
    val height: Int? = null,
    @SerialName("topMargin")
    val topMargin: Int? = null,
    @SerialName("startMargin")
    val startMargin: Int? = null,
    @SerialName("endMargin")
    val endMargin: Int? = null,
    @SerialName("bottomMargin")
    val bottomMargin: Int? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("size")
    val size: Int? = null,
    @SerialName("color")
    val color: String? = null,
    @SerialName("style")
    val style: List<String>? = null,
    @SerialName("url")
    val url: String? = null,
    @SerialName("radius")
    val radius: Int? = null,
    @SerialName("topLeftRadius")
    val topLeftRadius: Float? = null,
    @SerialName("topRightRadius")
    val topRightRadius: Float? = null,
    @SerialName("bottomLeftRadius")
    val bottomLeftRadius: Float? = null,
    @SerialName("bottomRightRadius")
    val bottomRightRadius: Float? = null,
    @SerialName("mode")
    val mode: Int? = null,
    @SerialName("tabs")
    val tabs: List<Tab>? = null,
    @SerialName("betweenMargin")
    val betweenMargin: Int? = null,
    @SerialName("orientation")
    val orientation: Int? = null,
    @SerialName("listData")
    val listData: List<Components>? = null,
    @SerialName("spanCount")
    val spanCount: Int? = null
)

@Serializable
data class Tab(
    @SerialName("name")
    val name: String? = null,
    @SerialName("component")
    val components: List<Component>? = null
)

sealed interface Components {
    val width: Int?
    val height: Int?
    val topMargin: Int?
    val startMargin: Int?
    val endMargin: Int?
    val bottomMargin: Int?

    @Serializable
    data class TextComponent(
        @SerialName("width")
        override val width: Int? = null,
        @SerialName("height")
        override val height: Int? = null,
        @SerialName("topMargin")
        override val topMargin: Int? = null,
        @SerialName("startMargin")
        override val startMargin: Int? = null,
        @SerialName("endMargin")
        override val endMargin: Int? = null,
        @SerialName("bottomMargin")
        override val bottomMargin: Int? = null,
        @SerialName("text")
        val text: String? = null,
        @SerialName("size")
        val size: Int? = null,
        @SerialName("color")
        val color: String? = null,
        @SerialName("style")
        val style: List<String>? = null,
    ) : Components

    @Serializable
    data class ImageComponent(
        @SerialName("width")
        override val width: Int? = null,
        @SerialName("height")
        override val height: Int? = null,
        @SerialName("topMargin")
        override val topMargin: Int? = null,
        @SerialName("startMargin")
        override val startMargin: Int? = null,
        @SerialName("endMargin")
        override val endMargin: Int? = null,
        @SerialName("bottomMargin")
        override val bottomMargin: Int? = null,
        @SerialName("url")
        val url: String? = null,
        @SerialName("radius")
        val radius: Int? = null,
        @SerialName("topLeftRadius")
        val topLeftRadius: Float? = null,
        @SerialName("topRightRadius")
        val topRightRadius: Float? = null,
        @SerialName("bottomLeftRadius")
        val bottomLeftRadius: Float? = null,
        @SerialName("bottomRightRadius")
        val bottomRightRadius: Float? = null
    ) : Components

    data class TabComponent(
        @SerialName("width")
        override val width: Int? = null,
        @SerialName("height")
        override val height: Int? = null,
        @SerialName("topMargin")
        override val topMargin: Int? = null,
        @SerialName("startMargin")
        override val startMargin: Int? = null,
        @SerialName("endMargin")
        override val endMargin: Int? = null,
        @SerialName("bottomMargin")
        override val bottomMargin: Int? = null,
        @SerialName("mode")
        val mode: Int? = null,
        @SerialName("tabs")
        val tabs: List<Tab>? = null
    ) : Components

    data class ListComponent(
        @SerialName("width")
        override val width: Int? = null,
        @SerialName("height")
        override val height: Int? = null,
        @SerialName("topMargin")
        override val topMargin: Int? = null,
        @SerialName("startMargin")
        override val startMargin: Int? = null,
        @SerialName("endMargin")
        override val endMargin: Int? = null,
        @SerialName("bottomMargin")
        override val bottomMargin: Int? = null,
        @SerialName("betweenMargin")
        val betweenMargin: Int? = null,
        @SerialName("spanCount")
        val spanCount: Int? = null,
        @SerialName("orientation")
        val orientation: Int? = null,
        @SerialName("url")
        val url: String? = null,
        @SerialName("url")
        val listData: List<Components>? = null
    ) : Components
}