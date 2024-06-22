plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.backupapi"
}

dependencies {
    implementation(project(":feature:docs:domain"))
}
