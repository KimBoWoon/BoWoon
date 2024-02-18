plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        project(":core:commonUtils"),
        project(":feat:permissionManager"),
        project(":feat:mediaStore")
    ).forEach {
        implementation(it)
    }
}