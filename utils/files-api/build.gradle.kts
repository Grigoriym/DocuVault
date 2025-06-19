plugins {
    alias(libs.plugins.docuvault.android.library)
}

android {
    namespace = "com.grappim.docuvault.utils.filesapi"
}

dependencies {
    implementation(projects.feature.docs.uiApi)
    implementation(projects.feature.docs.repoApi)
}
