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
        project(":core:commonUtils")
    ).forEach {
        implementation(it)
    }
}