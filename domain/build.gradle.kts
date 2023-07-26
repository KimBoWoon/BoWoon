plugins {
    id("bowoon.library")
    id("bowoon.hilt")
    id("bowoon.domain")
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.jetpack.datastore,
        libs.jetpack.paging.common,
        libs.hilt.android
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler
    ).forEach {
        kapt(it)
    }

    arrayOf(
        libs.test.junit
    ).forEach {
        testImplementation(it)
    }

    arrayOf(
        libs.test.junit.ext
    ).forEach {
        androidTestImplementation(it)
    }
}