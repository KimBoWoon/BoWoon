import java.text.SimpleDateFormat

plugins {
    id(Dependencies.BuildPlugins.application)
    id(Dependencies.BuildPlugins.parcelize)
    id(Dependencies.BuildPlugins.hilt)
    kotlin(Dependencies.BuildPlugins.kapt)
    kotlin(Dependencies.BuildPlugins.android)
}

android {
    namespace = Config.RssReader.namespace
    compileSdk = Config.Application.compileSdk

    defaultConfig {
        applicationId = Config.Application.applicationId + Config.RssReader.applicationIdSuffix
        minSdk = Config.Application.minSdk
        targetSdk = Config.Application.targetSdk
        versionCode = Config.RssReader.versionCode
        versionName = Config.RssReader.versionName

        testInstrumentationRunner = Config.Application.testInstrumentationRunner
    }

    val format = SimpleDateFormat(Config.Application.dateFormat)
    val buildTime = format.format(System.currentTimeMillis())
    setProperty(
        "archivesBaseName",
        "${Config.RssReader.appBundleName}${Config.RssReader.versionName}-$buildTime"
    )

    buildTypes {
        release {
            isMinifyEnabled = true
            isJniDebuggable = false
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile(Config.Application.defaultProguardFile),
                Config.Application.proguardFile
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        create(Config.BuildType.beta) {
            isMinifyEnabled = false
            isDebuggable = true
//            signingConfig = signingConfigs.getByName(Config.Sign.Debug.name)
        }
    }
    flavorDimensions.addAll(listOf(Config.Dimensions.mode))
    productFlavors {
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
//        compose = true
    }
}

dependencies {
    arrayOf(
        Dependencies.Jetpack.core,
        Dependencies.Jetpack.appcompat,
        Dependencies.Google.material,
        Dependencies.Layout.constraint,
        Dependencies.Hilt.hiltAndroid,
        Dependencies.Hilt.hiltNavigation,
        project(Dependencies.InnerModules.data),
        project(Dependencies.InnerModules.domain)
    ).forEach {
        implementation(it)
    }

    arrayOf(
        Dependencies.Hilt.hiltAndroidCompiler,
        Dependencies.Hilt.hiltCompiler,
    ).forEach {
        kapt(it)
    }

    arrayOf(
        Dependencies.Test.junit,
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