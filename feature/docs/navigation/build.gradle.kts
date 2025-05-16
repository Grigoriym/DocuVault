plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.kotlin.serialization)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.navigation"
}

dependencies {
    implementation(projects.core.navigation)
    implementation(projects.feature.docs.list)
    implementation(projects.feature.docs.details)
    implementation(projects.feature.docs.manager)

    implementation(libs.androidx.navigation.compose)
}
