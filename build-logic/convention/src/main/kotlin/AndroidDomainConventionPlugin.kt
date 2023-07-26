import com.android.build.api.dsl.LibraryExtension
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

class AndroidDomainConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
                apply("org.jetbrains.kotlin.plugin.parcelize")
            }

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    when (name) {
                        Config.Library.Domain.appName -> Config.Library.Domain
                        else -> throw RuntimeException("This is an undefined app. $name")
                    }.apply {
                        compileSdk = compileSdkVersion
                        minSdk = minSdkVersion
                        extensions.configure<LibraryExtension> {
                            this@configure.namespace = Config.Library.Domain.namespace
                            defaultConfig.targetSdk = targetSdkVersion
                            buildTypes {
                                debug {
                                    isMinifyEnabled = false
//                                    applicationIdSuffix = ".dev"
                                }
                            }
                            flavorDimensions.addAll(listOf(Config.Dimensions.mode))
                            productFlavors {
                                create(Config.Flavors.gpsAlarm) {
                                    dimension = Config.Dimensions.mode

                                    buildConfigField("String", "NAVER_MAPS_CLIENT_KEY", getProp("naver_maps_client_key"))
                                    buildConfigField("String", "NAVER_MAPS_CLIENT_SECRET_KEY", getProp("naver_maps_client_secret_key"))
                                }
                                create(Config.Flavors.lol) {
                                    dimension = Config.Dimensions.mode

                                    buildConfigField("String", "riotApiKey", getProp("riot_api_key"))
                                }
                                create(Config.Flavors.practice) {
                                    dimension = Config.Dimensions.mode
                                }
                                create(Config.Flavors.rssReader) {
                                    dimension = Config.Dimensions.mode
                                }
                            }

                            sourceSets {
                                getByName(Config.SourceSet.main) {
                                    getByName(Config.Flavors.gpsAlarm) {
                                        manifest.srcFile("src/gpsAlarm/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/gpsAlarm/java"))
                                    }
                                    getByName(Config.Flavors.lol) {
                                        manifest.srcFile("src/lol/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/lol/java"))
                                    }
                                    getByName(Config.Flavors.practice) {
                                        manifest.srcFile("src/practice/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/practice/java"))
                                    }
                                    getByName(Config.Flavors.rssReader) {
                                        manifest.srcFile("src/rssReader/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/rssReader/java"))
                                    }
                                }
                                getByName(Config.SourceSet.debug) {
                                    getByName(Config.Flavors.gpsAlarm) {
                                        manifest.srcFile("src/gpsAlarm/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/gpsAlarm/java"))
                                    }
                                    getByName(Config.Flavors.lol) {
                                        manifest.srcFile("src/lol/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/lol/java"))
                                    }
                                    getByName(Config.Flavors.practice) {
                                        manifest.srcFile("src/practice/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/practice/java"))
                                    }
                                    getByName(Config.Flavors.rssReader) {
                                        manifest.srcFile("src/rssReader/AndroidManifest.xml")
                                        java.setSrcDirs(listOf("src/base/java", "src/rssReader/java"))
                                    }
                                }
                            }
                        }
                    }
                }

                configureKotlinAndroid(this)
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
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