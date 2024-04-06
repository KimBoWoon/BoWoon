plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.glide,
        libs.jetpack.paging,
        libs.jetpack.activity,
        libs.jetpack.fragment,
        project(":core:network"),
        project(":core:commonUtils"),
        project(":core:imageLoader")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.glide.compiler
    ).forEach {
        kapt(it)
    }
}