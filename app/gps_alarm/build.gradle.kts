plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.swiperefresh.layout,
        libs.jetpack.navigation.ktx,
        libs.jetpack.navigation.ui.ktx,
        libs.jetpack.datastore,
        libs.jetpack.lifecycle,
        libs.jetpack.startup,
        libs.google.location,
        libs.hilt.android,
        libs.hilt.navigation,
        libs.glide,
        libs.firebase.message,
        libs.naver.map,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.retrofit2,
        project(":core:dataManager"),
        project(":core:commonUtils"),
        project(":core:network"),
        project(":core:ui")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.glide.compiler
    ).forEach {
        kapt(it)
    }
}