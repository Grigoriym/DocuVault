plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.repoimpl"
}

dependencies {
    implementation(projects.utils.dateTimeApi)
    implementation(projects.common.async)

    implementation(projects.feature.docs.domain)
    implementation(projects.feature.docs.repoApi)
    implementation(projects.feature.docs.db)

    implementation(projects.feature.docgroup.db)
    implementation(projects.feature.docgroup.repoApi)
    implementation(projects.feature.docgroup.domain)
}
