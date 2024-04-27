plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.repo"
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":utils:datetime"))
    implementation(project(":utils:files"))
    implementation(project(":uikit"))
    implementation(project(":common:async"))
    implementation(project(":data:db"))
    implementation(project(":data:storage"))

    implementation(libs.timber)
}
