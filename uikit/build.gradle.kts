plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.uikit"
}

dependencies {
    implementation(project(":feature:group:domain"))
    implementation(project(":feature:docs:ui-api"))
    implementation(project(":core:navigation"))

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)

    implementation(libs.coil)
}
