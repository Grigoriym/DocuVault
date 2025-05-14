plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docgroup.repoimpl"
}

dependencies {
    implementation(projects.common.async)
    implementation(projects.feature.docgroup.domain)
    implementation(projects.feature.docgroup.repoApi)
    implementation(projects.feature.docgroup.db)
}
