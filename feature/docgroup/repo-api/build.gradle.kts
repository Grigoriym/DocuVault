plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docgroup.repoapi"
}

dependencies {
    implementation(projects.feature.docgroup.db)
}
