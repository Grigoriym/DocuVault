plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.group.repoapi"
}

dependencies {
    implementation(project(":feature:group:domain"))
    implementation(project(":feature:group:db"))
}
