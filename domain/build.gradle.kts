plugins {
    id(Dependencies.BuildPlugins.library)
    kotlin(Dependencies.BuildPlugins.android)
    kotlin(Dependencies.BuildPlugins.kapt)
    id(Dependencies.BuildPlugins.hilt)
    id(Dependencies.BuildPlugins.parcelize)
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
        Dependencies.Serialization.kotlin,
        Dependencies.Serialization.converter,
        Dependencies.Hilt.hiltAndroid,
    ).forEach {
        implementation(it)
    }

    arrayOf(
        Dependencies.Hilt.hiltAndroidCompiler,
        Dependencies.Hilt.hiltCompiler
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