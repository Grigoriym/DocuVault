plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.utils.files"
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.timber)
}
