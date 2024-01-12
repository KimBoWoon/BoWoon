plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.jetpack.viewModel,
        libs.jetpack.liveData,
        libs.jetpack.activity,
        libs.jetpack.fragment,
        libs.jetpack.navigation.ktx,
        libs.jetpack.navigation.ui.ktx,
        libs.jetpack.datastore,
        libs.jetpack.work.manager,
        libs.hilt.android,
        libs.hilt.navigation,
        libs.hilt.work.manager,
        libs.glide,
        libs.firebase.message,
        libs.retrofit2,
        project(":core:dataManager"),
        project(":core:commonUtils"),
        project(":core:network")
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.glide.compiler
    ).forEach {
        kapt(it)
    }
}