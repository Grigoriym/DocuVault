plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.utils.datetime"
}

dependencies {
    implementation(projects.utils.dateTimeApi)
}
