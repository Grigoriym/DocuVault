plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.cleanerimpl"
}

dependencies {
    implementation(project(":data:cleaner-api"))
    implementation(project(":feature:docs:domain"))
    implementation(project(":common:async"))
    implementation(project(":utils:files"))
    implementation(project(":feature:docs:repo-api"))
    implementation(project(":data:db-api"))
}
