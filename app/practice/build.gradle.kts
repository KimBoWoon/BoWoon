import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id(Dependencies.BuildPlugins.application)
    kotlin(Dependencies.BuildPlugins.android)
    kotlin(Dependencies.BuildPlugins.kapt)
    id(Dependencies.BuildPlugins.hilt)
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
    val flavorList = listOf(Config.Variant.beta, Config.Variant.full)
    flavorDimensions += Config.Flavors.environmentDimension
    productFlavors {
        flavorList.forEach { flavor ->
            create(flavor) {
                dimension = Config.Flavors.environmentDimension
                applicationIdSuffix = ".$flavor"

                if (flavor == Config.Variant.full) {
                    signingConfig = signingConfigs.getByName(Config.Sign.Release.name)
                }
            }
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
        "androidx.core:core-ktx:1.8.0",
        "androidx.appcompat:appcompat:1.5.0",
        "com.google.android.material:material:1.6.1",
        "androidx.constraintlayout:constraintlayout:2.1.4",
        Dependencies.Hilt.hiltAndroid,
        project(Dependencies.InnerModules.data),
        project(Dependencies.InnerModules.domain)
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
        "junit:junit:4.13.2"
    ).forEach {
        testImplementation(it)
    }

    arrayOf(
        "androidx.test.ext:junit:1.1.3",
        "androidx.test.espresso:espresso-core:3.4.0"
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