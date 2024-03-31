plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.serialization.kotlin,
        libs.serialization.converter,
        project(":core:network"),
        project(":core:commonUtils"),
        project(":core:imageLoader")
    ).forEach {
        implementation(it)
    }
}