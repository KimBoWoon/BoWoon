object Dependencies {
    object BuildPlugins {
        const val application = "com.android.application"
        const val android = "android"
        const val library = "com.android.library"
        const val parcelize = "kotlin-parcelize"
        const val hilt = "dagger.hilt.android.plugin"
        const val kapt = "kapt"
        const val jvm = "jvm"
        const val serialization = "plugin.serialization"
        const val safeArgs = "androidx.navigation.safeargs.kotlin"
        const val googleService = "com.google.gms.google-services"
        const val firebasePerformance = "com.google.firebase.firebase-perf"
    }

    object Gradle {
        const val gradle = "com.android.tools.build:gradle:${Versions.Gradle.gradle}"
        const val plugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.Kotlin.kotlin}"
    }

    object Plugins {
        const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.Plugins.safeArgs}"
    }

    object Google {
        const val material = "com.google.android.material:material:${Versions.Google.material}"
        const val location = "com.google.android.gms:play-services-location:${Versions.Google.location}"
    }

    object Firebase {
        const val googleService = "com.google.gms:google-services:${Versions.Firebase.googleService}"
        const val gradle = "com.android.tools.build:gradle:${Versions.Firebase.gradle}"
        const val performancePlugin = "com.google.firebase:perf-plugin:${Versions.Firebase.performancePlugin}"
        const val bom = "com.google.firebase:firebase-bom:${Versions.Firebase.bom}"
        const val performance = "com.google.firebase:firebase-perf-ktx"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
        const val message = "com.google.firebase:firebase-messaging-ktx"
    }

    object Jetpack {
        const val core = "androidx.core:core-ktx:${Versions.Jetpack.core}"
        const val appcompat = "androidx.appcompat:appcompat:${Versions.Jetpack.appcompat}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Jetpack.lifecycle}"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Jetpack.lifecycle}"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.Jetpack.lifecycle}"
        const val activity = "androidx.activity:activity-ktx:${Versions.Jetpack.activity}"
        const val fragment = "androidx.fragment:fragment-ktx:${Versions.Jetpack.fragment}"
        const val navigationKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.Jetpack.navigation}"
        const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.Jetpack.navigation}"
        const val datastore = "androidx.datastore:datastore-preferences:${Versions.Jetpack.datastore}"
        const val datastoreCore = "androidx.datastore:datastore-preferences-core:${Versions.Jetpack.datastore}"
        const val paging = "androidx.paging:paging-runtime:${Versions.Jetpack.paging}"
        const val pagingCommon = "androidx.paging:paging-common:${Versions.Jetpack.paging}"
        const val room = "androidx.room:room-runtime:${Versions.Jetpack.room}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.Jetpack.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.Jetpack.room}"
        const val workManager = "androidx.work:work-runtime-ktx:${Versions.Jetpack.workManager}"
        const val roomPaging = "androidx.room:room-paging:${Versions.Jetpack.room}"
        const val splash = "androidx.core:core-splashscreen:${Versions.Jetpack.splash}"
    }

    object Layout {
        const val constraint = "androidx.constraintlayout:constraintlayout:${Versions.Layout.constraint}"
    }

    object Retrofit2 {
        const val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.Retrofit2.retrofit2}"
    }

    object OkHttp {
        const val bom = "com.squareup.okhttp3:okhttp-bom:${Versions.OkHttp.okHttp}"
        const val okhttp = "com.squareup.okhttp3:okhttp"
        const val logging = "com.squareup.okhttp3:logging-interceptor"
        const val profiler = "com.localebro:okhttpprofiler:${Versions.OkHttp.profiler}"
    }

    object Serialization {
        const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-serialization:${Versions.Kotlin.kotlin}"
        const val kotlin = "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Serialization.kotlin}"
        const val converter = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${Versions.Serialization.converter}"
    }

    object Glide {
        const val glide = "com.github.bumptech.glide:glide:${Versions.Glide.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.Glide.glide}"
    }

    object Hilt {
        const val plugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.Hilt.hilt}"
        const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.Hilt.hilt}"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.Hilt.hilt}"
        const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.Hilt.compiler}"
        const val hiltNavigation = "androidx.hilt:hilt-navigation-fragment:${Versions.Hilt.hiltNavigation}"
        const val hiltWorkManager = "androidx.hilt:hilt-work:${Versions.Hilt.hiltWorkManager}"
    }

    object Compose {
        const val bom = "androidx.compose:compose-bom:${Versions.Compose.bom}"
        const val material3 = "androidx.compose.material3:material3"
        const val material2 = "androidx.compose.material:material"
        const val preview = "androidx.compose.ui:ui-tooling-preview"
        const val previewUiTooling = "androidx.compose.ui:ui-tooling"
        const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4"
        const val uiTestManifest = "androidx.compose.ui:ui-test-manifest"
        const val materialIconsCore = "androidx.compose.material:material-icons-core"
        const val materialIconsExtended = "androidx.compose.material:material-icons-extended"
        const val material3WindowSizeClass = "androidx.compose.material3:material3-window-size-class"
        const val activity = "androidx.activity:activity-compose:${Versions.Compose.activity}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.Compose.viewModel}"
        const val navigation = "androidx.navigation:navigation-compose:${Versions.Compose.navigation}"
        const val hiltNavigation = "androidx.hilt:hilt-navigation-compose:${Versions.Compose.hiltNavigation}"
        const val paging = "androidx.paging:paging-compose:${Versions.Compose.paging}"
        const val viewBinding = "androidx.compose.ui:ui-viewbinding:${Versions.Compose.viewBinding}"
        const val iconExtended = "androidx.compose.material:material-icons-extended:${Versions.Compose.iconExtended}"
        const val lottie = "com.airbnb.android:lottie-compose:${Versions.Compose.lottie}"
    }

    object Orbit{
        const val core = "org.orbit-mvi:orbit-core:${Versions.Orbit.orbit}"
        const val viewModel = "org.orbit-mvi:orbit-viewmodel:${Versions.Orbit.orbit}"
        const val compose = "org.orbit-mvi:orbit-compose:${Versions.Orbit.orbit}"
        const val test = "org.orbit-mvi:orbit-test:${Versions.Orbit.orbit}"
    }

    object Accompanist {
        const val webview =  "com.google.accompanist:accompanist-webview:${Versions.Accompanist.webview}"
        const val pullToRefresh = "com.google.accompanist:accompanist-swiperefresh:${Versions.Accompanist.pullToRefresh}"
    }

    object Coroutine {
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Coroutine.core}"
    }

    object Naver {
        const val map = "com.naver.maps:map-sdk:${Versions.Naver.map}"
    }

    object ThreeTenABP {
        const val core = "com.jakewharton.threetenabp:threetenabp:${Versions.ThreeTenABP.core}"
    }

    object TikXml {
        const val annotation = "com.tickaroo.tikxml:annotation:${Versions.TikXml.core}"
        const val retrofitConverter = "com.tickaroo.tikxml:retrofit-converter:${Versions.TikXml.core}"
        const val core = "com.tickaroo.tikxml:core:${Versions.TikXml.core}"
        const val processor = "com.tickaroo.tikxml:processor:${Versions.TikXml.core}"
    }

    object Test {
        const val junit = "junit:junit:${Versions.Test.junit}"
        const val junitExt = "androidx.test.ext:junit:${Versions.Test.junitExt}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.Test.espresso}"
    }

    object InnerModules {
        const val data = ":data"
        const val domain = ":domain"
    }
}