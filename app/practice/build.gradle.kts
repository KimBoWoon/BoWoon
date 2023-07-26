plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.jetpack.appcompat,
        libs.jetpack.paging,
        libs.jetpack.room.ktx,
        libs.jetpack.navigation.ktx,
        libs.jetpack.navigation.ui.ktx,
        libs.google.material,
        libs.constraint.layout,
        libs.hilt.android,
        project(":data"),
        project(":domain")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.jetpack.room.compiler
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