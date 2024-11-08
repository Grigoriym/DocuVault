plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.cleanerapi"
}

dependencies {
    implementation(project(":feature:docs:domain"))
}
