plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
}

android {
    namespace = "com.grappim.docuvault.feature.group.navigation"
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":feature:group:list"))
    implementation(project(":feature:group:details"))
    implementation(project(":feature:group:manager"))

    implementation(libs.androidx.navigation.compose)
}
