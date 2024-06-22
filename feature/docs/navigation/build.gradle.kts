plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.library.compose)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.navigation"
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":feature:docs:list"))
    implementation(project(":feature:docs:details"))
    implementation(project(":feature:docs:manager"))

    implementation(libs.androidx.navigation.compose)
}
