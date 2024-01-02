plugins {
    id("bowoon.library")
}

android {
    namespace = "com.bowoon.ui"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.jetpack.appcompat,
        libs.constraint.layout,
        project(":core:commonUtils")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.test.junit,
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