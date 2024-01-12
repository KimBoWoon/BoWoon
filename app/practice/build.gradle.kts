plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.jetpack.paging,
        libs.jetpack.room.ktx,
        libs.jetpack.navigation.ktx,
        libs.jetpack.navigation.ui.ktx,
        libs.hilt.android,
        libs.retrofit2,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.glide,
        project(":core:dataManager"),
        project(":core:commonUtils"),
        project(":core:network"),
        project(":core:imageLoader")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.jetpack.room.compiler
    ).forEach {
        kapt(it)
    }
}