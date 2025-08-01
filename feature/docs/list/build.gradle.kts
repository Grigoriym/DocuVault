plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.list"
}

dependencies {
    implementation(projects.uikit)
    implementation(projects.core.navigation)
    implementation(projects.feature.docs.domain)
    implementation(projects.feature.docs.repoApi)
    implementation(projects.feature.docs.uiApi)
    implementation(projects.utils.filesApi)

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.runtime.compose)
    implementation(libs.androidx.viewmodel.compose)

    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil)
}
