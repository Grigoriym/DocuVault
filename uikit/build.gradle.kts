plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.uikit"
}

dependencies {
    implementation(projects.feature.docgroup.domain)
    implementation(projects.feature.docs.uiApi)
    implementation(projects.core.navigation)

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.material3)

    implementation(libs.coil)
}
