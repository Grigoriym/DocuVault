plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.utils.files"
}

dependencies {
    implementation(project(":utils:date-time"))
    implementation(project(":uikit"))
    implementation(project(":common:async"))
    implementation(project(":feature:group:domain"))
    implementation(project(":feature:docs:ui-api"))
    implementation(project(":feature:docs:domain"))

    implementation(libs.timber)
    implementation(libs.androidx.compose.ui)
}
