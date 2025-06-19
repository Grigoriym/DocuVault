plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
}

android {
    namespace = "com.grappim.docuvault.feature.docgroup.utilapi"
}

dependencies {
    implementation(projects.feature.docgroup.repoApi)
}
