plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.backupimpl"
}

dependencies {
    implementation(projects.data.backupDb)
    implementation(projects.data.backupApi)
    implementation(projects.common.async)
    implementation(projects.feature.docs.domain)
}
