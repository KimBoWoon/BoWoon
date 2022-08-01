import java.io.FileInputStream
import java.util.*

plugins {
    id(Dependencies.BuildPlugins.library)
    kotlin(Dependencies.BuildPlugins.android)
    kotlin(Dependencies.BuildPlugins.kapt)
    id(Dependencies.BuildPlugins.hilt)
}

android {
    compileSdk = Config.Application.compileSdk

    defaultConfig {
        minSdk = Config.Application.minSdk
        targetSdk = Config.Application.targetSdk

        testInstrumentationRunner = Config.Application.testInstrumentationRunner
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "riotApiKey", getProp("riot_api_key"))
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile(Config.Application.defaultProguardFile), Config.Application.proguardFile)
        }
        debug {
            isMinifyEnabled = false
        }
    }
//    flavorDimensions += Config.ProductFlavors.flavorDimension
//    productFlavors {
//        create(Config.ProductFlavors.beta) {
//            dimension = Config.ProductFlavors.flavorDimension
//        }
//        create(Config.ProductFlavors.full) {
//            dimension = Config.ProductFlavors.flavorDimension
//        }
//    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = Config.Application.jvmVersion
    }
    buildFeatures {
        dataBinding = true
    }

    packagingOptions {
        resources.excludes.add("META-INF/DEPENDENCIES")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/LICENSE")
    }
}

dependencies {
    arrayOf(
        Dependencies.Jetpack.core,
        Dependencies.Retrofit2.retrofit2,
        Dependencies.OkHttp.bom,
        Dependencies.OkHttp.okhttp,
        Dependencies.OkHttp.logging,
        Dependencies.OkHttp.profiler,
        Dependencies.Serialization.kotlin,
        Dependencies.Serialization.converter,
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
        Dependencies.Glide.glideCompiler
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
fun getProp(propertyKey: String): String {
    if (!this::prop.isInitialized) {
        prop = Properties().apply {
            load(FileInputStream(File("./sign", "local.properties")))
        }
    }

    return prop.getProperty(propertyKey)
}