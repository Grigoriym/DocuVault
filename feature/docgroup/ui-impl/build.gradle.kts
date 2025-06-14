plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docgroup.utilimpl"
}

dependencies {
    implementation(projects.uikit)
    implementation(projects.common.async)
    implementation(projects.feature.docgroup.repoApi)
    implementation(projects.feature.docgroup.uiApi)
}
