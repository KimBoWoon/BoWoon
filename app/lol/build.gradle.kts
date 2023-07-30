plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.constraint.layout,
        libs.jetpack.core,
        libs.jetpack.appcompat,
        libs.jetpack.viewModel,
        libs.jetpack.liveData,
        libs.jetpack.activity,
        libs.jetpack.fragment,
        libs.jetpack.navigation.ktx,
        libs.jetpack.navigation.ui.ktx,
        libs.jetpack.datastore,
        libs.jetpack.work.manager,
        libs.google.material,
        libs.hilt.android,
        libs.hilt.navigation,
        libs.hilt.work.manager,
        libs.glide.glide,
        libs.firebase.performance,
        libs.firebase.analytics,
        libs.firebase.message,
        platform(libs.firebase.bom),
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.glide.glideCompiler
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