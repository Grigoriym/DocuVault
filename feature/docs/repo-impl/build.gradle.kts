plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.docs.repoimpl"
}

dependencies {
    implementation(project(":utils:date-time"))
    implementation(project(":common:async"))

    implementation(project(":feature:docs:domain"))
    implementation(project(":feature:docs:repo-api"))
    implementation(project(":feature:docs:db"))

    implementation(project(":feature:group:db"))
    implementation(project(":feature:group:repo-api"))
    implementation(project(":feature:group:domain"))
}
