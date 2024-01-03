plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.jetpack.appcompat,
        libs.google.material,
        libs.constraint.layout,
        libs.hilt.android,
        libs.hilt.navigation,
        libs.retrofit2,
        libs.tikxml.annotation,
        libs.jsoup,
        libs.firebase.performance,
        libs.firebase.analytics,
        libs.glide,
        project(":core:dataManager"),
        project(":core:commonUtils"),
        project(":core:network"),
        project(":core:ui"),
        project(":core:imageLoader"),
        platform(libs.firebase.bom)
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.tikxml.processor
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
        libs.test.espresso,
    ).forEach {
        androidTestImplementation(it)
    }
}