package com.bowoon.convention

object Config {
    object ApplicationSetting {
        const val COMPILE_SDK_VERSION = 34
        const val MIN_SDK_VERSION = 21
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val defaultProguardFile = "proguard-android-optimize.txt"
        const val proguardFile = "proguard-rules.pro"
        const val dateFormat = "HHmmss"
    }

    object Library {
        const val MIN_SDK_VERSION = 21
        const val COMPILE_SDK_VERSION = 34
    }

    sealed class Application(
        val appName: String,
        val applicationId: String,
        val compileSdkVersion: Int = ApplicationSetting.COMPILE_SDK_VERSION,
        val minSdkVersion: Int = ApplicationSetting.MIN_SDK_VERSION,
        val targetSdkVersion: Int = ApplicationSetting.COMPILE_SDK_VERSION,
        val versionCode: Int,
        val versionName: String,
    ) {
        object GpsAlarm : Application(
            appName = "gps_alarm",
            applicationId = "com.bowoon.gpsAlarm",
            compileSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            minSdkVersion = ApplicationSetting.MIN_SDK_VERSION,
            targetSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            versionCode = 1,
            versionName = "1.0.0"
        ) {
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
        }

        object RssReader : Application(
            appName = "rss_reader",
            applicationId = "com.bowoon.rss_reader",
            compileSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            minSdkVersion = ApplicationSetting.MIN_SDK_VERSION,
            targetSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            versionCode = 1,
            versionName = "1.0.0"
        ) {
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
        }

        object Lol : Application(
            appName = "lol",
            applicationId = "com.bowoon.lol",
            compileSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            minSdkVersion = ApplicationSetting.MIN_SDK_VERSION,
            targetSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            versionCode = 1,
            versionName = "1.0.0"
        ) {
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
        }

        object Practice : Application(
            appName = "practice",
            applicationId = "com.bowoon.practice",
            compileSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            minSdkVersion = ApplicationSetting.MIN_SDK_VERSION,
            targetSdkVersion = ApplicationSetting.COMPILE_SDK_VERSION,
            versionCode = 1,
            versionName = "1.0.0"
        ) {
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
        }
    }
}