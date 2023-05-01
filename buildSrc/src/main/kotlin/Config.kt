object Config {
    object Application {
        const val compileSdk = 33
        const val minSdk = 23
        const val targetSdk = 33
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val jvmVersion = "11"
        const val applicationId = "com.bowoon"
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

    object BuildType {
        const val release = "release"
        const val debug = "debug"
        const val beta = "beta"
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
    }

    object Practice {
        const val appBundleName = "practice-v"
        const val versionCode = 1
        const val versionName = "1.0.0"
        const val applicationIdSuffix = ".practice"
    }

    object Lol {
        const val appBundleName = "lol-v"
        const val versionCode = 1
        const val versionName = "1.0.0"
        const val applicationIdSuffix = ".lol"
    }

    object GpsAlarm {
        const val appBundleName = "gps_alarm-v"
        const val versionCode = 1
        const val versionName = "1.0.0"
        const val applicationIdSuffix = ".gpsAlarm"
        const val namespace = "com.bowoon.android.gps_alarm"
        const val naverMapsClientName = "NAVER_MAPS_CLIENT_KEY"
        const val naverMapsClientKey = "naver_maps_client_key"
    }
}