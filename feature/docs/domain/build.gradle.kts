plugins {
    alias(libs.plugins.docuvault.java.library)
}

dependencies {
    implementation(projects.feature.docgroup.domain)
}
