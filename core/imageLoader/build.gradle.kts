plugins {
    id("bowoon.library")
}

android {
    namespace = "com.bowoon.imageLoader"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    arrayOf(
        libs.glide,
        libs.coil,
        project(":core:commonUtils")
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