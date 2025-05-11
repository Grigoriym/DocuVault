plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.kotlin.serialization)
}

android {
    namespace = "com.grappim.docuvault.feature.settings.navigation"
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":feature:settings:ui"))

    implementation(libs.androidx.navigation.compose)
}
