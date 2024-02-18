plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    arrayOf(
        libs.jetpack.media3.exoplayer,
        libs.jetpack.media3.exoplayer.session,
        libs.jetpack.media3.exoplayer.ui,
        project(":core:commonUtils"),
        project(":feat:permissionManager"),
        project(":feat:mediaStore")
    ).forEach {
        implementation(it)
    }
}