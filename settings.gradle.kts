pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}

rootProject.name = "BoWoon"

include(":app")
include(":app:lol")
include(":app:practice")
include(":app:gps_alarm")
include(":app:rss_reader")
include(":core")
include(":core:dataManager")
include(":core:network")
include(":core:commonUtils")
include(":core:imageLoader")
include(":core:ui")
include(":feat:permissionManager")
include(":feat:timer")
