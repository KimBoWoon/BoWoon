plugins {
    id("bowoon.library")
}

android {
    namespace = "com.bowoon.commomUtils"

    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.recyclerView,
        libs.recyclerView.selection,
        libs.viewpager2,
        libs.google.material
    ).forEach {
        implementation(it)
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