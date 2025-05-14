plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.feature.docgroup.manager"
}

dependencies {
    implementation(projects.uikit)
    implementation(projects.core.navigation)
    implementation(projects.feature.docgroup.domain)
    implementation(projects.feature.docgroup.repoApi)

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.runtime.compose)
    implementation(libs.androidx.viewmodel.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.compose.color.picker.android)
}
