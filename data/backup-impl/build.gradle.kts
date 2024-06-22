plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.backupimpl"
}

dependencies {
    implementation(project(":data:backup-db"))
    implementation(project(":data:backup-api"))
    implementation(project(":common:async"))
    implementation(project(":feature:docs:domain"))
}
