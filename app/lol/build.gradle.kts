import java.io.FileInputStream
import java.util.*
import java.text.*

plugins {
    id(Dependencies.BuildPlugins.application)
    kotlin(Dependencies.BuildPlugins.android)
    kotlin(Dependencies.BuildPlugins.kapt)
    id(Dependencies.BuildPlugins.hilt)
}

android {
    compileSdk = Config.Application.compileSdk

    defaultConfig {
        applicationId = Config.Application.applicationId + Config.Lol.applicationIdSuffix
        minSdk = Config.Application.minSdk
        targetSdk = Config.Application.targetSdk
        versionCode = Config.Lol.versionCode
        versionName = Config.Lol.versionName

        testInstrumentationRunner = Config.Application.testInstrumentationRunner
    }

    val format = SimpleDateFormat(Config.Application.dateFormat)
    val buildTime = format.format(System.currentTimeMillis())
    setProperty("archivesBaseName", "${Config.Application.appBundleName}${Config.Lol.versionName}-$buildTime")

    signingConfigs {
        create(Config.Sign.Release.name) {
            storeFile = file(getProp(Config.Sign.Release.storeFile))
            storePassword = getProp(Config.Sign.Release.storePassword)
            keyAlias = getProp(Config.Sign.Release.keyAlias)
            keyPassword = getProp(Config.Sign.Release.keyPassword)
        }
    }
    buildTypes {
        getByName(Config.BuildType.release) {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile(Config.Application.defaultProguardFile), Config.Application.proguardFile)
        }
        getByName(Config.BuildType.debug) {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
//    val flavorList = listOf(Config.ProductFlavors.beta, Config.ProductFlavors.full)
//    flavorDimensions += Config.ProductFlavors.flavorDimension
    flavorDimensions.add(Config.ProductFlavors.flavorDimension)
    productFlavors {
        create(Config.ProductFlavors.beta) {
            dimension = Config.ProductFlavors.flavorDimension
            applicationIdSuffix = ".${Config.ProductFlavors.beta}"
        }
        create(Config.ProductFlavors.full) {
            dimension = Config.ProductFlavors.flavorDimension
            applicationIdSuffix = ".${Config.ProductFlavors.full}"
        }
//        flavorList.forEach { flavor ->
//            create(flavor) {
//                dimension = Config.ProductFlavors.flavorDimension
//                applicationIdSuffix = ".$flavor"
//
//                if (flavor == Config.ProductFlavors.full) {
//                    signingConfig = signingConfigs.getByName(Config.Sign.Release.name)
//                }
//            }
//        }
    }
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
        resources.excludes.add("META-INF/LICENSE")
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/license.txt")
        resources.excludes.add("META-INF/NOTICE")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("META-INF/notice.txt")
        resources.excludes.add("META-INF/ASL2.0")
        resources.excludes.add("META-INF/*.kotlin_module")
    }
}

dependencies {
    arrayOf(
        Dependencies.Layout.constraint,
        Dependencies.Jetpack.core,
        Dependencies.Jetpack.appcompat,
        Dependencies.Jetpack.material,
        Dependencies.Jetpack.viewModel,
        Dependencies.Jetpack.liveData,
        Dependencies.Jetpack.activity,
        Dependencies.Jetpack.fragment,
        Dependencies.Hilt.hiltAndroid,
        Dependencies.Glide.glide,
        project(Dependencies.InnerModules.data),
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
        Dependencies.Test.junitExt,
        Dependencies.Test.espresso,
    ).forEach {
        androidTestImplementation(it)
    }
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