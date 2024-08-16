plugins {
    id("bowoon.application")
    id("bowoon.hilt")
}

dependencies {
    implementation(libs.jetpack.core)
    implementation(libs.jetpack.appcompat)
    implementation(libs.google.material)
    implementation(libs.activity)
    implementation(libs.constraint.layout)
    implementation(libs.jetpack.datastore)
    implementation(project(":core:dataManager"))
    implementation(project(":core:commonUtils"))
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.junit.ext)
    androidTestImplementation(libs.test.espresso)
}