plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.manager"
}

dependencies {
    implementation(projects.uikit)
    implementation(projects.utils.ui)
    implementation(projects.utils.filesApi)
    implementation(projects.feature.docs.domain)
    implementation(projects.feature.docs.uiApi)
    implementation(projects.feature.docgroup.domain)
    implementation(projects.feature.docgroup.uiApi)
    implementation(projects.feature.docgroup.repoApi)
    implementation(projects.feature.docs.repoApi)
    implementation(projects.core.navigation)
    implementation(projects.data.backupApi)
    implementation(projects.data.cleanerApi)

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.runtime.compose)
    implementation(libs.androidx.viewmodel.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.timber)
}
