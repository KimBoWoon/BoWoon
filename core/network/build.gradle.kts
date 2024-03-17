plugins {
    id("bowoon.library")
    id("bowoon.hilt")
}

android {
    namespace = "com.bowoon.network"
}

dependencies {
    arrayOf(
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.retrofit2,
        libs.okhttp.okhttp,
        libs.okhttp.logging,
        libs.okhttp.profiler,
        libs.tikxml.annotation,
        libs.tikxml.retrofitConverter,
        libs.tikxml.core,
        project(":core:commonUtils"),
        platform(libs.okhttp.bom)
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.tikxml.processor
    ).forEach {
        kapt(it)
    }
}