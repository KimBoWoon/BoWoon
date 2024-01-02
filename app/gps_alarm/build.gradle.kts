plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.constraint.layout,
        libs.swiperefresh.layout,
        libs.jetpack.navigation.ktx,
        libs.jetpack.navigation.ui.ktx,
        libs.jetpack.core,
        libs.jetpack.datastore,
        libs.jetpack.lifecycle,
        libs.jetpack.startup,
        libs.google.location,
        libs.hilt.android,
        libs.hilt.navigation,
        libs.glide,
        libs.firebase.performance,
        libs.firebase.analytics,
        libs.firebase.message,
        libs.firebase.crashlytics,
        libs.naver.map,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.retrofit2,
        project(":core:dataManager"),
        project(":core:commonUtils"),
        project(":core:network"),
        project(":core:ui"),
        platform(libs.firebase.bom)
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

    arrayOf(
        libs.test.junit
    ).forEach {
        testImplementation(it)
    }

    arrayOf(
        libs.test.junit.ext,
        libs.test.espresso
    ).forEach {
        androidTestImplementation(it)
    }
}