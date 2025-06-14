plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.repoapi"
}

dependencies {
    implementation(projects.feature.docs.db)
    implementation(projects.feature.docgroup.repoApi)
}
