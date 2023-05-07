import java.io.FileInputStream
import java.util.*

plugins {
    id(Dependencies.BuildPlugins.library)
    kotlin(Dependencies.BuildPlugins.android)
    kotlin(Dependencies.BuildPlugins.kapt)
    id(Dependencies.BuildPlugins.parcelize)
    id(Dependencies.BuildPlugins.hilt)
    //    kotlin(Dependencies.BuildPlugins.jvm) // version "1.6.20" // or kotlin("multiplatform") or any other kotlin plugin
    kotlin(Dependencies.BuildPlugins.serialization) // version "1.6.20"
}

android {
    compileSdk = Config.Application.compileSdk

    defaultConfig {
        minSdk = Config.Application.minSdk
        targetSdk = Config.Application.targetSdk

        testInstrumentationRunner = Config.Application.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile(Config.Application.defaultProguardFile), Config.Application.proguardFile)
        }
        debug {
            isMinifyEnabled = false
        }
        create(Config.BuildType.beta) {
            isMinifyEnabled = false
        }
    }
    flavorDimensions.addAll(listOf(Config.Dimensions.mode))
    productFlavors {
        create(Config.Flavors.lol) {
            dimension = Config.Dimensions.mode

            buildConfigField("String", "riotApiKey", getProp("riot_api_key"))
        }
        create(Config.Flavors.practice) {
            dimension = Config.Dimensions.mode
        }
        create(Config.Flavors.gpsAlarm) {
            dimension = Config.Dimensions.mode

            buildConfigField("String", "NAVER_MAPS_CLIENT_KEY", getProp("naver_maps_client_key"))
            buildConfigField("String", "NAVER_MAPS_CLIENT_SECRET_KEY", getProp("naver_maps_client_secret_key"))
        }
        create(Config.Flavors.rssReader) {
            dimension = Config.Dimensions.mode
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = Config.Application.jvmVersion
    }
    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
            )
        )
    }
    sourceSets {
        getByName(Config.SourceSet.main) {
            getByName(Config.Flavors.lol) {
                manifest.srcFile("src/lol/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/lol/java"))
            }
            getByName(Config.Flavors.practice) {
                manifest.srcFile("src/practice/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/practice/java"))
            }
            getByName(Config.Flavors.gpsAlarm) {
                manifest.srcFile("src/gpsAlarm/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/gpsAlarm/java"))
            }
            getByName(Config.Flavors.rssReader) {
                manifest.srcFile("src/rssReader/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/rssReader/java"))
            }
        }
        getByName(Config.SourceSet.debug) {
            getByName(Config.Flavors.lol) {
                manifest.srcFile("src/lol/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/lol/java"))
            }
            getByName(Config.Flavors.practice) {
                manifest.srcFile("src/practice/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/practice/java"))
            }
            getByName(Config.Flavors.gpsAlarm) {
                manifest.srcFile("src/gpsAlarm/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/gpsAlarm/java"))
            }
            getByName(Config.Flavors.rssReader) {
                manifest.srcFile("src/rssReader/AndroidManifest.xml")
                java.setSrcDirs(listOf("src/base/java", "src/rssReader/java"))
            }
        }
    }
    namespace = "com.data"
}

dependencies {
    arrayOf(
        Dependencies.Jetpack.core,
        Dependencies.Jetpack.datastore,
        Dependencies.Jetpack.paging,
        Dependencies.Jetpack.roomKtx,
        Dependencies.Google.material,
        Dependencies.Retrofit2.retrofit2,
        Dependencies.OkHttp.bom,
        Dependencies.OkHttp.okhttp,
        Dependencies.OkHttp.logging,
        Dependencies.OkHttp.profiler,
        Dependencies.Serialization.kotlin,
        Dependencies.Serialization.converter,
        Dependencies.TikXml.annotation,
        Dependencies.TikXml.retrofitConverter,
        Dependencies.TikXml.core,
        Dependencies.Hilt.hiltAndroid,
        Dependencies.Coroutine.core,
        Dependencies.Glide.glide,
        project(Dependencies.InnerModules.domain)
    ).forEach {
        implementation(it)
    }

    arrayOf(
        Dependencies.Hilt.hiltAndroidCompiler,
        Dependencies.Hilt.hiltCompiler,
        Dependencies.TikXml.processor,
        Dependencies.Glide.glideCompiler,
        Dependencies.Jetpack.roomCompiler
    ).forEach {
        kapt(it)
    }

    arrayOf(
        Dependencies.Test.junit
    ).forEach {
        testImplementation(it)
    }

    arrayOf(
        Dependencies.Test.junitExt
    ).forEach {
        androidTestImplementation(it)
    }
}

kapt {
    correctErrorTypes = true
}

lateinit var prop: Properties
fun getProp(propertyKey: String): String =
    runCatching {
        if (!this::prop.isInitialized) {
            prop = Properties().apply {
                load(FileInputStream(File("./sign", "local.properties")))
            }
        }
        prop.getProperty(propertyKey)
    }.getOrDefault("")