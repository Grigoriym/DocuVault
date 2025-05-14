plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.utils.files"
}

dependencies {
    implementation(projects.utils.dateTimeApi)
    implementation(projects.uikit)
    implementation(projects.common.async)
    implementation(projects.feature.docgroup.domain)
    implementation(projects.feature.docs.uiApi)
    implementation(projects.feature.docs.domain)

    implementation(libs.timber)
    implementation(libs.androidx.compose.ui)
}
