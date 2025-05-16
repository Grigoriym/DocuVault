plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.repoapi"
}

dependencies {
    implementation(projects.feature.docs.domain)
    implementation(projects.feature.docs.db)
}
