plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.cleanerimpl"
}

dependencies {
    implementation(projects.data.cleanerApi)
    implementation(projects.feature.docs.domain)
    implementation(projects.common.async)
    implementation(projects.utils.filesApi)
    implementation(projects.feature.docs.repoApi)
    implementation(projects.data.dbApi)
}
