plugins {
    id("bowoon.library")
    id("bowoon.hilt")
    id("bowoon.data")
}

dependencies {
    arrayOf(
        libs.jetpack.core,
        libs.jetpack.datastore,
        libs.jetpack.paging,
        libs.jetpack.room.ktx,
        libs.google.material,
        libs.retrofit2,
        libs.okhttp.bom,
        libs.okhttp.okhttp,
        libs.okhttp.logging,
        libs.okhttp.profiler,
        libs.serialization.kotlin,
        libs.serialization.converter,
        libs.tikxml.annotation,
        libs.tikxml.retrofitConverter,
        libs.tikxml.core,
        libs.hilt.android,
        libs.coroutine.core,
        libs.glide.glide,
    ).forEach {
        implementation(it)
    }

    arrayOf(
        libs.hilt.android.compiler,
        libs.hilt.compiler,
        libs.tikxml.processor,
        libs.glide.glideCompiler,
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
        libs.test.junit.ext
    ).forEach {
        androidTestImplementation(it)
    }
}