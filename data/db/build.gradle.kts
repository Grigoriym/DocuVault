plugins {
    alias(libs.plugins.docuvault.android.library)
    alias(libs.plugins.docuvault.android.hilt)
}

android {
    namespace = "com.grappim.docuvault.data.db"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = true
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
        arg("room.expandProjection", "true")
    }
}

dependencies {
    implementation(project(":utils:files"))
    implementation(project(":utils:date-time-api"))
    implementation(project(":common:async"))
    implementation(project(":feature:group:db"))
    implementation(project(":feature:docs:db"))
    implementation(project(":data:backup-db"))
    implementation(project(":data:db-api"))

    implementation(libs.androidx.security.crypto)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.testing)
}
