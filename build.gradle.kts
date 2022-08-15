// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Dependencies.Gradle.gradle)
        classpath(Dependencies.Gradle.plugin)
        classpath(Dependencies.Serialization.kotlinPlugin)
        classpath(Dependencies.Hilt.plugin)
        classpath(Dependencies.Plugins.safeArgs)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}