object Config {
    object Application {
        const val compileSdk = 32
        const val minSdk = 23
        const val targetSdk = 32
        const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val jvmVersion = "11"
        const val applicationId = "com.bowoon"
        const val defaultProguardFile = "proguard-android-optimize.txt"
        const val proguardFile = "proguard-rules.pro"
        const val appBundleName = "lol-v"
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
        }
    }

    object BuildType {
        const val release = "release"
        const val debug = "debug"
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
    }

    object Practice {
        const val versionCode = 1
        const val versionName = "1.0.0"
        const val applicationIdSuffix = ".practice"
    }

    object Lol {
        const val versionCode = 1
        const val versionName = "1.0.0"
        const val applicationIdSuffix = ".lol"
    }
}