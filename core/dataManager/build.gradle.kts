plugins {
    id("bowoon.library")
    id("bowoon.hilt")
}

android {
    namespace = "com.bowoon.dataManager"
}

dependencies {
    arrayOf(
        libs.jetpack.datastore,
        libs.hilt.android
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
    ).forEach {
        kapt(it)
    }
}