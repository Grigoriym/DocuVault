plugins {
    alias(libs.plugins.docuvault.java.library)
}

dependencies {
    implementation(project(":feature:group:domain"))
}
