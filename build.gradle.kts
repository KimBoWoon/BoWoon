// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.4.0")
        classpath("com.google.gms:google-services:4.3.15")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.5")
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