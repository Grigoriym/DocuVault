plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.utils.files"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":utils:date-time"))

    implementation(libs.timber)
}
