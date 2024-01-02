plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.jetpack.appcompat,
        libs.jetpack.paging,
        libs.jetpack.room.ktx,
        libs.jetpack.navigation.ktx,
        libs.jetpack.navigation.ui.ktx,
        libs.google.material,
        libs.constraint.layout,
        libs.hilt.android,
        libs.retrofit2,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.firebase.performance,
        libs.firebase.analytics,
        libs.glide,
        project(":core:dataManager"),
        project(":core:commonUtils"),
        project(":core:network"),
        project(":core:imageLoader"),
        platform(libs.firebase.bom)
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