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
    implementation(projects.utils.filesApi)
    implementation(projects.uikit)
    implementation(projects.common.async)
    implementation(projects.feature.docgroup.repoApi)
    implementation(projects.feature.docs.uiApi)
    implementation(projects.feature.docs.repoApi)

    implementation(libs.timber)
    testImplementation(libs.robolectric)
}
