plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.kotlin.serialization)
}

android {
    namespace = "com.grappim.docuvault.feature.settings.navigation"
}

dependencies {
    implementation(projects.core.navigation)
    implementation(projects.feature.settings.ui)

    implementation(libs.androidx.navigation.compose)
}
