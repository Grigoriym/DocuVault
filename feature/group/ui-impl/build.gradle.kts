plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.group.utilimpl"
}

dependencies {
    implementation(project(":feature:group:domain"))
    implementation(project(":feature:group:ui-api"))
    implementation(project(":common:async"))
    implementation(project(":uikit"))

    implementation(libs.androidx.compose.ui)
}
