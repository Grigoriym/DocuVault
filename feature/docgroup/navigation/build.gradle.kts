plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
}

android {
    namespace = "com.grappim.docuvault.feature.docgroup.navigation"
}

dependencies {
    implementation(projects.core.navigation)
    implementation(projects.feature.docgroup.list)
    implementation(projects.feature.docgroup.details)
    implementation(projects.feature.docgroup.manager)

    implementation(libs.androidx.navigation.compose)
}
