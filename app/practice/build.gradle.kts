import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id(Dependencies.BuildPlugins.application)
    kotlin(Dependencies.BuildPlugins.android)
    kotlin(Dependencies.BuildPlugins.kapt)
    id(Dependencies.BuildPlugins.hilt)
    id(Dependencies.BuildPlugins.safeArgs)
}

android {
    compileSdk = Config.Application.compileSdk

    defaultConfig {
        applicationId = Config.Application.applicationId + Config.Practice.applicationIdSuffix
        minSdk = Config.Application.minSdk
        targetSdk = Config.Application.targetSdk
        versionCode = Config.Practice.versionCode
        versionName = Config.Practice.versionName

        testInstrumentationRunner = Config.Application.testInstrumentationRunner
    }

    val format = SimpleDateFormat(Config.Application.dateFormat)
    val buildTime = format.format(System.currentTimeMillis())
    setProperty("archivesBaseName", "${Config.Application.appBundleName}${Config.Practice.versionName}-$buildTime")

    signingConfigs {
        create(Config.Sign.Release.name) {
            storeFile = file(getProp(Config.Sign.Release.storeFile))
            storePassword = getProp(Config.Sign.Release.storePassword)
            keyAlias = getProp(Config.Sign.Release.keyAlias)
            keyPassword = getProp(Config.Sign.Release.keyPassword)
        }
        getByName(Config.Sign.Debug.name) {
            storeFile = file(getProp(Config.Sign.Debug.storeFile))
            storePassword = getProp(Config.Sign.Debug.storePassword)
            keyAlias = getProp(Config.Sign.Debug.keyAlias)
            keyPassword = getProp(Config.Sign.Debug.keyPassword)
        }
    }
    buildTypes {
        getByName(Config.BuildType.release) {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile(Config.Application.defaultProguardFile), Config.Application.proguardFile)
            signingConfig = signingConfigs.getByName(Config.Sign.Release.name)
        }
        getByName(Config.BuildType.debug) {
            isMinifyEnabled = false
            isDebuggable = true
        }
        create(Config.BuildType.beta) {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName(Config.Sign.Debug.name)
        }
    }
    flavorDimensions.addAll(listOf(Config.Dimensions.mode))
    productFlavors {
        create(Config.Flavors.lol) {
            dimension = Config.Dimensions.mode
        }
        create(Config.Flavors.practice) {
            dimension = Config.Dimensions.mode
        }
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
        Dependencies.Jetpack.core,
        Dependencies.Jetpack.appcompat,
        Dependencies.Jetpack.paging,
        Dependencies.Jetpack.roomKtx,
        Dependencies.Jetpack.navigationKtx,
        Dependencies.Jetpack.navigationUiKtx,
        Dependencies.Google.material,
        Dependencies.Layout.constraint,
        Dependencies.Hilt.hiltAndroid,
        project(Dependencies.InnerModules.data),
        project(Dependencies.InnerModules.domain)
    ).forEach {
        implementation(it)
    }

    arrayOf(
        Dependencies.Hilt.hiltAndroidCompiler,
        Dependencies.Hilt.hiltCompiler,
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
        Dependencies.Test.junitExt,
        Dependencies.Test.espresso
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