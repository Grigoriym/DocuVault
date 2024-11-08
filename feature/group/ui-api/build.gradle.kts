plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
}

android {
    namespace = "com.grappim.docuvault.feature.group.utilapi"
}

dependencies {
    implementation(project(":feature:group:domain"))

    implementation(libs.androidx.compose.ui)
}
