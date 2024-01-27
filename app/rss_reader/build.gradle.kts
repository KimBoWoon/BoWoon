plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.hilt.navigation,
        libs.retrofit2,
        libs.tikxml.annotation,
        libs.jsoup,
        libs.glide,
        libs.serialization.kotlin,
        project(":core:dataManager"),
        project(":core:commonUtils"),
        project(":core:network"),
        project(":core:ui"),
        project(":core:imageLoader"),
        project(":feat:timer")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.tikxml.processor
    ).forEach {
        kapt(it)
    }
}