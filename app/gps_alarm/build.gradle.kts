import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.*

plugins {
    id(Dependencies.BuildPlugins.application)
    kotlin(Dependencies.BuildPlugins.android)
    kotlin(Dependencies.BuildPlugins.kapt)
    id(Dependencies.BuildPlugins.hilt)
    id(Dependencies.BuildPlugins.googleService)
    id(Dependencies.BuildPlugins.firebasePerformance)
    kotlin(Dependencies.BuildPlugins.serialization)
}

android {
    namespace = Config.GpsAlarm.namespace
    compileSdk = Config.Application.compileSdk

    defaultConfig {
        applicationId = Config.Application.applicationId + Config.GpsAlarm.applicationIdSuffix
        minSdk = Config.Application.minSdk
        targetSdk = Config.Application.targetSdk
        versionCode = Config.GpsAlarm.versionCode
        versionName = Config.GpsAlarm.versionName

        testInstrumentationRunner = Config.Application.testInstrumentationRunner

        buildConfigField("String", Config.GpsAlarm.naverMapsClientName, getProp(Config.GpsAlarm.naverMapsClientKey))
    }

    val format = SimpleDateFormat(Config.Application.dateFormat)
    val buildTime = format.format(System.currentTimeMillis())
    setProperty("archivesBaseName", "${Config.Application.appBundleName}${Config.GpsAlarm.versionName}-$buildTime")

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
        create(Config.Flavors.gpsAlarm) {
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.Compose.compose
    }

    packagingOptions {
        resources.excludes.addAll(
            listOf(
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module"
            )
        )
    }
}

dependencies {
    arrayOf(
        Dependencies.Compose.material2,
        Dependencies.Compose.preview,
        Dependencies.Compose.activity,
        Dependencies.Compose.viewModel,
        Dependencies.Compose.navigation,
        Dependencies.Compose.hiltNavigation,
        Dependencies.Layout.constraint,
        Dependencies.Jetpack.core,
        Dependencies.Jetpack.appcompat,
        Dependencies.Jetpack.liveData,
        Dependencies.Jetpack.activity,
        Dependencies.Jetpack.fragment,
        Dependencies.Jetpack.datastore,
        Dependencies.Jetpack.workManager,
        Dependencies.Google.material,
        Dependencies.Google.location,
        Dependencies.Hilt.hiltAndroid,
        Dependencies.Hilt.hiltNavigation,
        Dependencies.Hilt.hiltWorkManager,
        Dependencies.Glide.glide,
        Dependencies.Firebase.performance,
        Dependencies.Firebase.analytics,
        Dependencies.Firebase.message,
        Dependencies.Naver.map,
        Dependencies.Serialization.kotlin,
        Dependencies.Serialization.converter,
        platform(Dependencies.Compose.bom),
        platform(Dependencies.Firebase.bom),
        project(Dependencies.InnerModules.data),
        project(Dependencies.InnerModules.domain)
    ).forEach {
        implementation(it)
    }

    arrayOf(
        Dependencies.Compose.previewUiTooling,
        Dependencies.Compose.uiTestManifest
    ).forEach {
        debugImplementation(it)
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
        Dependencies.Compose.uiTestJunit4,
        platform(Dependencies.Compose.bom)
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