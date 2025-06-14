plugins {
    alias(libs.plugins.docuvault.android.library)
}

android {
    namespace = "com.grappim.docuvault.utils.androidapi"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
