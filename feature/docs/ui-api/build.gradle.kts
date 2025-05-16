plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.uiapi"
}

dependencies {
    implementation(libs.androidx.compose.ui.graphics)
}
