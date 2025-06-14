plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.cleanerimpl"
}

dependencies {
    implementation(projects.data.cleanerApi)
    implementation(projects.data.dbApi)
    implementation(projects.feature.docs.repoApi)
    implementation(projects.feature.docgroup.repoApi)
    implementation(projects.common.async)
    implementation(projects.utils.filesApi)

    implementation(libs.timber)
}
