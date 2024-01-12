import com.android.build.gradle.LibraryExtension
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

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("org.jetbrains.kotlin.kapt")
                apply("org.jetbrains.kotlin.plugin.parcelize")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    compileSdk = Config.Library.MIN_SDK_VERSION
                    minSdk = Config.Library.COMPILE_SDK_VERSION
                    buildTypes {
                        release {
                            isMinifyEnabled = true
                            isJniDebuggable = false
                        }
                        debug {
                            isMinifyEnabled = false
                        }
                    }

                    buildConfigField("String", "NAVER_MAPS_CLIENT_KEY", getProp("naver_maps_client_key"))
                    buildConfigField("String", "NAVER_MAPS_CLIENT_SECRET_KEY", getProp("naver_maps_client_secret_key"))
                    buildConfigField("String", "riotApiKey", getProp("riot_api_key"))

//                    when (appName) {
//                        Config.Application.GpsAlarm.appName -> {
//                        }
//                        Config.Application.Lol.appName -> {
//                        }
//                    }
                }

                configureKotlinAndroid(this)
            }
            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "implementation"(libs.findLibrary("jetpack.core").get())
                "implementation"(libs.findLibrary("jetpack.appcompat").get())
                "implementation"(libs.findLibrary("google.material").get())
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