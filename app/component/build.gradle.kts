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
        libs.coil,
        "androidx.preference:preference-ktx:1.2.0",
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