// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        listOf(
            Dependencies.Gradle.gradle,
            Dependencies.Gradle.plugin,
            Dependencies.Serialization.kotlinPlugin,
            Dependencies.Hilt.plugin,
            Dependencies.Plugins.safeArgs,
            Dependencies.Firebase.googleService,
            Dependencies.Firebase.gradle,
            Dependencies.Firebase.performancePlugin
        ).forEach {
            classpath(it)
        }
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}