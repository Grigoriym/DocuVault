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
    implementation(projects.utils.files)
    implementation(projects.utils.dateTimeApi)
    implementation(projects.common.async)
    implementation(projects.feature.docgroup.db)
    implementation(projects.feature.docs.db)
    implementation(projects.data.backupDb)
    implementation(projects.data.dbApi)

    implementation(libs.androidx.security.crypto)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.testing)
}
