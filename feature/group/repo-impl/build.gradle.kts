plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.feature.group.repoimpl"
}

dependencies {
    implementation(project(":feature:group:domain"))
    implementation(project(":feature:group:repo-api"))
    implementation(project(":feature:group:db"))
    implementation(project(":common:async"))
}
