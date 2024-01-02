plugins {
    id("bowoon.library")
    id("bowoon.hilt")
}

android {
    namespace = "com.bowoon.network"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.retrofit2,
        libs.okhttp.bom,
        libs.okhttp.okhttp,
        libs.okhttp.logging,
        libs.okhttp.profiler,
        libs.hilt.android,
        libs.tikxml.annotation,
        libs.tikxml.retrofitConverter,
        libs.tikxml.core,
        project(":core:commonUtils")
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
        libs.test.espresso
    ).forEach {
        androidTestImplementation(it)
    }
}