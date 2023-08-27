import com.android.build.api.dsl.ApplicationExtension
import com.bowoon.convention.Config
import com.bowoon.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import java.io.File
import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Properties

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.kapt")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("org.jetbrains.kotlin.plugin.parcelize")
                apply("com.google.firebase.firebase-perf")
                apply("com.google.firebase.crashlytics")
                apply("com.google.gms.google-services")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    when (name) {
                        Config.Application.GpsAlarm.appName -> Config.Application.GpsAlarm
                        Config.Application.RssReader.appName -> Config.Application.RssReader
                        Config.Application.Lol.appName -> Config.Application.Lol
                        Config.Application.Practice.appName -> Config.Application.Practice
                        else -> throw RuntimeException("This is an undefined app. $name")
                    }.apply {
                        compileSdk = compileSdkVersion
                        minSdk = minSdkVersion
                        extensions.configure<ApplicationExtension> {
                            namespace = applicationId
                            defaultConfig.applicationId = applicationId
                            defaultConfig.targetSdk = targetSdkVersion
                            defaultConfig.versionName = versionName
                            defaultConfig.versionCode = versionCode
                            testInstrumentationRunner = Config.ApplicationSetting.testInstrumentationRunner
                        }

                        SimpleDateFormat(Config.ApplicationSetting.dateFormat, Locale.getDefault()).run {
                            setProperty("archivesBaseName", "${appName}-v${versionName}-${format(System.currentTimeMillis())}")
                        }
                    }

                    flavorDimensions.addAll(listOf(Config.Dimensions.mode))
                    productFlavors {
                        create(Config.Flavors.gpsAlarm) {
                            dimension = Config.Dimensions.mode
                        }
                        create(Config.Flavors.lol) {
                            dimension = Config.Dimensions.mode
                        }
                        create(Config.Flavors.practice) {
                            dimension = Config.Dimensions.mode
                        }
                        create(Config.Flavors.rssReader) {
                            dimension = Config.Dimensions.mode
                        }
                    }
                }

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
                    release {
                        isMinifyEnabled = true
                        isShrinkResources = true
                        isDebuggable = false
                        proguardFiles(
                            getDefaultProguardFile(Config.ApplicationSetting.defaultProguardFile),
                            Config.ApplicationSetting.proguardFile
                        )
                        signingConfig = signingConfigs.getByName(Config.Sign.Release.name)
                    }
                    debug {
                        isMinifyEnabled = false
//                        applicationIdSuffix = ".dev"
                    }
                }

                configureKotlinAndroid(this)
                buildFeatures {
                    viewBinding = true
                    dataBinding = true
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                add("implementation", project(":domain"))
                add("implementation", project(":data"))
            }
        }
    }

    lateinit var prop: Properties
    private fun getProp(propertyKey: String): String =
        runCatching {
            if (!this::prop.isInitialized) {
                prop = Properties().apply {
                    load(FileInputStream(File("./sign", "local.properties")))
                }
            }

            prop.getProperty(propertyKey)
        }.getOrDefault("\"key not found\"")
}