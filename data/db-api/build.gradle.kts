plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.dbapi"
}

dependencies {
    implementation(projects.feature.docs.db)
    implementation(projects.data.backupDb)
    implementation(projects.feature.docgroup.db)
}
