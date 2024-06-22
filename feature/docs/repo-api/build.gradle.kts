plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.repoapi"
}

dependencies {
    implementation(project(":feature:docs:domain"))
    implementation(project(":feature:docs:db"))
}
