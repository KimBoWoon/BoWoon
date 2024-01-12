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
        libs.recyclerView,
        libs.recyclerView.selection,
        libs.viewpager2,
    ).forEach {
        implementation(it)
    }
}