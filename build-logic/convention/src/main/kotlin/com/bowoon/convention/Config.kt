package com.bowoon.convention

object Config {
    object ApplicationSetting {
        const val compileSdk = 33
        const val minSdk = 23
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val defaultProguardFile = "proguard-android-optimize.txt"
        const val proguardFile = "proguard-rules.pro"
        const val dateFormat = "HHmmss"
    }

    object Sign {
        object Release {
            const val name = "release"
            const val storeFile = "store_file_path"
            const val storePassword = "store_password"
            const val keyAlias = "key_alias"
            const val keyPassword = "key_password"
        }

        object Debug {
            const val name = "debug"
            const val storeFile = "store_file_path"
            const val storePassword = "store_password"
            const val keyAlias = "key_alias"
            const val keyPassword = "key_password"
        }
    }

    object Dimensions {
        const val mode = "mode"
    }

    object SourceSet {
        const val main = "main"
        const val debug = "debug"
    }

    object Flavors {
        const val lol = "lol"
        const val practice = "practice"
        const val gpsAlarm = "gpsAlarm"
        const val rssReader = "rssReader"
    }

    sealed class Library(
        val appName: String,
        val namespace: String,
        val compileSdkVersion: Int = 33,
        val minSdkVersion: Int = 21,
        val targetSdkVersion: Int = 33,
    ) {
        object Data : Library(
            appName = "data",
            namespace = "com.data",
            compileSdkVersion = 33,
            minSdkVersion = 21,
            targetSdkVersion = 33,
        )

        object Domain : Library(
            appName = "domain",
            namespace = "com.domain",
            compileSdkVersion = 33,
            minSdkVersion = 21,
            targetSdkVersion = 33,
        )
    }

    sealed class Application(
        val appName: String,
        val applicationId: String,
        val compileSdkVersion: Int = 33,
        val minSdkVersion: Int = 21,
        val targetSdkVersion: Int = 33,
        val versionCode: Int,
        val versionName: String,
    ) {
        object GpsAlarm : Application(
            appName = "gps_alarm",
            applicationId = "com.bowoon.gpsAlarm",
            compileSdkVersion = 33,
            minSdkVersion = 21,
            targetSdkVersion = 33,
            versionCode = 1,
            versionName = "1.0.0"
        )

        object RssReader : Application(
            appName = "rss_reader",
            applicationId = "com.bowoon.rss_reader",
            compileSdkVersion = 33,
            minSdkVersion = 21,
            targetSdkVersion = 33,
            versionCode = 1,
            versionName = "1.0.0"
        )

        object Lol : Application(
            appName = "lol",
            applicationId = "com.bowoon.lol",
            compileSdkVersion = 33,
            minSdkVersion = 21,
            targetSdkVersion = 33,
            versionCode = 1,
            versionName = "1.0.0"
        )

        object Practice : Application(
            appName = "practice",
            applicationId = "com.bowoon.practice",
            compileSdkVersion = 33,
            minSdkVersion = 21,
            targetSdkVersion = 33,
            versionCode = 1,
            versionName = "1.0.0"
        )
    }
}