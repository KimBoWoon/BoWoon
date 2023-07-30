plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.jetpack.appcompat,
        libs.google.material,
        libs.constraint.layout,
        libs.hilt.android,
        libs.hilt.navigation,
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
        libs.test.junit.ext,
        libs.test.espresso,
    ).forEach {
        androidTestImplementation(it)
    }
}