plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.utils.androidimpl"
}

dependencies {
    implementation(projects.utils.androidApi)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.timber)

    testImplementation(libs.robolectric)
}
