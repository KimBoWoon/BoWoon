plugins {
    id("bowoon.application")
    id("bowoon.hilt")
    id("bowoon.application.compose")
}

dependencies {
    arrayOf(
        libs.compose.material2,
        libs.compose.preview,
        libs.compose.activity,
        libs.compose.viewModel,
        libs.compose.navigation,
        libs.compose.hilt.navigation,
        libs.compose.view.binding,
        libs.compose.icon.extended,
        libs.accompanist.webview,
        libs.constraint.layout,
        libs.jetpack.core,
        libs.jetpack.datastore,
        libs.jetpack.lifecycle,
//        libs.jetpack.splash,
        libs.google.location,
        libs.hilt.android,
        libs.hilt.navigation,
        libs.glide.glide,
        libs.firebase.performance,
        libs.firebase.analytics,
        libs.firebase.message,
        libs.firebase.crashlytics,
        libs.naver0map,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.orbit.core,
        libs.orbit.viewModel,
        libs.orbit.compose,
        platform(libs.compose.bom),
        platform(libs.firebase.bom),
        project(":data"),
        project(":domain")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.compose.preview.ui.tooling,
        libs.compose.ui.test.manifest
    ).forEach {
        debugImplementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.glide.glideCompiler
    ).forEach {
        kapt(it)
    }

    arrayOf(
        libs.test.junit,
        libs.orbit.test
    ).forEach {
        testImplementation(it)
    }

    arrayOf(
        libs.test.junit.ext,
        libs.test.espresso,
        libs.compose.ui.test.junit4,
        platform(libs.compose.bom)
    ).forEach {
        androidTestImplementation(it)
    }
}