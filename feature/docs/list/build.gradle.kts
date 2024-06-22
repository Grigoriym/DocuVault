plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.list"
}

dependencies {
    implementation(project(":uikit"))
    implementation(project(":feature:docs:domain"))
    implementation(project(":feature:docs:repo-api"))
    implementation(project(":utils:files"))

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.runtime.compose)
    implementation(libs.androidx.viewmodel.compose)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.coil)
}
