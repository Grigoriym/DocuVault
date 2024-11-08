plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.dbapi"
}

dependencies {
    implementation(project(":feature:docs:db"))
    implementation(project(":data:backup-db"))
    implementation(project(":feature:group:db"))
}
