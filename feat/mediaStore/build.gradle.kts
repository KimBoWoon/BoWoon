plugins {
    id("bowoon.library")
    id("bowoon.hilt")
}

android {
    namespace = "com.bowoon.mediaStore"
}

dependencies {
    arrayOf(
        project(":core:commonUtils")
    ).forEach {
        implementation(it)
    }
}