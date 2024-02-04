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
                apply("androidx.navigation.safeargs.kotlin")
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

                            signingConfigs {
                                when (appName) {
                                    Config.Application.GpsAlarm.appName -> {
                                        create(Config.Application.GpsAlarm.Sign.Release.name) {
                                            storeFile = file(getProp(Config.Application.GpsAlarm.Sign.Release.storeFile))
                                            storePassword = getProp(Config.Application.GpsAlarm.Sign.Release.storePassword)
                                            keyAlias = getProp(Config.Application.GpsAlarm.Sign.Release.keyAlias)
                                            keyPassword = getProp(Config.Application.GpsAlarm.Sign.Release.keyPassword)
                                        }
                                        getByName(Config.Application.GpsAlarm.Sign.Debug.name) {
                                            storeFile = file(getProp(Config.Application.GpsAlarm.Sign.Debug.storeFile))
                                            storePassword = getProp(Config.Application.GpsAlarm.Sign.Debug.storePassword)
                                            keyAlias = getProp(Config.Application.GpsAlarm.Sign.Debug.keyAlias)
                                            keyPassword = getProp(Config.Application.GpsAlarm.Sign.Debug.keyPassword)
                                        }
                                    }
                                    Config.Application.Lol.appName -> {
                                        create(Config.Application.Lol.Sign.Release.name) {
                                            storeFile = file(getProp(Config.Application.Lol.Sign.Release.storeFile))
                                            storePassword = getProp(Config.Application.Lol.Sign.Release.storePassword)
                                            keyAlias = getProp(Config.Application.Lol.Sign.Release.keyAlias)
                                            keyPassword = getProp(Config.Application.Lol.Sign.Release.keyPassword)
                                        }
                                        getByName(Config.Application.Lol.Sign.Debug.name) {
                                            storeFile = file(getProp(Config.Application.Lol.Sign.Debug.storeFile))
                                            storePassword = getProp(Config.Application.Lol.Sign.Debug.storePassword)
                                            keyAlias = getProp(Config.Application.Lol.Sign.Debug.keyAlias)
                                            keyPassword = getProp(Config.Application.Lol.Sign.Debug.keyPassword)
                                        }
                                    }
                                    Config.Application.Practice.appName -> {
                                        create(Config.Application.Practice.Sign.Release.name) {
                                            storeFile = file(getProp(Config.Application.Practice.Sign.Release.storeFile))
                                            storePassword = getProp(Config.Application.Practice.Sign.Release.storePassword)
                                            keyAlias = getProp(Config.Application.Practice.Sign.Release.keyAlias)
                                            keyPassword = getProp(Config.Application.Practice.Sign.Release.keyPassword)
                                        }
                                        getByName(Config.Application.Practice.Sign.Debug.name) {
                                            storeFile = file(getProp(Config.Application.Practice.Sign.Debug.storeFile))
                                            storePassword = getProp(Config.Application.Practice.Sign.Debug.storePassword)
                                            keyAlias = getProp(Config.Application.Practice.Sign.Debug.keyAlias)
                                            keyPassword = getProp(Config.Application.Practice.Sign.Debug.keyPassword)
                                        }
                                    }
                                    Config.Application.RssReader.appName -> {
                                        create(Config.Application.RssReader.Sign.Release.name) {
                                            storeFile = file(getProp(Config.Application.RssReader.Sign.Release.storeFile))
                                            storePassword = getProp(Config.Application.RssReader.Sign.Release.storePassword)
                                            keyAlias = getProp(Config.Application.RssReader.Sign.Release.keyAlias)
                                            keyPassword = getProp(Config.Application.RssReader.Sign.Release.keyPassword)
                                        }
                                        getByName(Config.Application.RssReader.Sign.Debug.name) {
                                            storeFile = file(getProp(Config.Application.RssReader.Sign.Debug.storeFile))
                                            storePassword = getProp(Config.Application.RssReader.Sign.Debug.storePassword)
                                            keyAlias = getProp(Config.Application.RssReader.Sign.Debug.keyAlias)
                                            keyPassword = getProp(Config.Application.RssReader.Sign.Debug.keyPassword)
                                        }
                                    }
                                }
                            }
                        }

//                        SimpleDateFormat(Config.ApplicationSetting.dateFormat, Locale.getDefault()).run {
//                            setProperty("archivesBaseName", "${appName}-v${versionName}-${format(System.currentTimeMillis())}")
//                        }

                        when (appName) {
                            Config.Application.GpsAlarm.appName -> {
                                buildConfigField("String", "NAVER_MAPS_CLIENT_KEY", getProp("naver_maps_client_key"))
                                buildConfigField("String", "NAVER_MAPS_CLIENT_SECRET_KEY", getProp("naver_maps_client_secret_key"))
                            }
                            Config.Application.Lol.appName -> {
                                buildConfigField("String", "riotApiKey", getProp("riot_api_key"))
                            }
                        }
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
                        when (namespace) {
                            Config.Application.GpsAlarm.applicationId -> {
                                signingConfigs.getByName(Config.Application.GpsAlarm.Sign.Release.name)
                            }
                            Config.Application.Lol.applicationId -> {
                                signingConfigs.getByName(Config.Application.Lol.Sign.Release.name)
                            }
                            Config.Application.Practice.applicationId -> {
                                signingConfigs.getByName(Config.Application.Practice.Sign.Release.name)
                            }
                            Config.Application.RssReader.applicationId -> {
                                signingConfigs.getByName(Config.Application.RssReader.Sign.Release.name)
                            }
                        }
                    }
                    debug {
                        isMinifyEnabled = false
//                        applicationIdSuffix = ".dev"
                        when (namespace) {
                            Config.Application.GpsAlarm.applicationId -> {
                                signingConfigs.getByName(Config.Application.GpsAlarm.Sign.Debug.name)
                            }
                            Config.Application.Lol.applicationId -> {
                                signingConfigs.getByName(Config.Application.Lol.Sign.Debug.name)
                            }
                            Config.Application.Practice.applicationId -> {
                                signingConfigs.getByName(Config.Application.Practice.Sign.Debug.name)
                            }
                            Config.Application.RssReader.applicationId -> {
                                signingConfigs.getByName(Config.Application.RssReader.Sign.Debug.name)
                            }
                        }
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
                "implementation"(libs.findLibrary("jetpack.core").get())
                "implementation"(libs.findLibrary("jetpack.appcompat").get())
                "implementation"(libs.findLibrary("google.material").get())
                "implementation"(libs.findLibrary("constraint.layout").get())
                "implementation"(libs.findLibrary("firebase.performance").get())
                "implementation"(libs.findLibrary("firebase.analytics").get())
                "implementation"(libs.findLibrary("firebase.crashlytics").get())
                "implementation"(platform(libs.findLibrary("firebase.bom").get()))
                "testImplementation"(libs.findLibrary("test.junit").get())
                "androidTestImplementation"(libs.findLibrary("test.junit.ext").get())
                "androidTestImplementation"(libs.findLibrary("test.espresso").get())
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