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
        libs.okhttp.bom,
        libs.okhttp.okhttp,
        libs.okhttp.logging,
        libs.okhttp.profiler,
        libs.tikxml.annotation,
        libs.tikxml.retrofitConverter,
        libs.tikxml.core,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.firebase.performance,
        libs.firebase.analytics,
        platform(libs.firebase.bom)
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler
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