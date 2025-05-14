plugins {
    alias(libs.plugins.docuvault.android.application)
    alias(libs.plugins.docuvault.kotlin.serialization)
    alias(libs.plugins.docuvault.android.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.moduleGraphAssertion)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.grappim.docuvault"

    defaultConfig {
        applicationId = "com.grappim.docuvault"
        testApplicationId = "com.grappim.docuvault.tests"

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

// It will find a gplay build only if start building specifically gplay build
val isGooglePlayBuild = project.gradle.startParameter.taskRequests.toString().contains("Gplay")

logger.lifecycle("${project.gradle.startParameter.taskRequests}")
project.gradle.startParameter.taskRequests.forEach {
    logger.lifecycle("🔥 Detected Gradle Task: $it")
}

if (isGooglePlayBuild) {
    logger.lifecycle("✅ Applying Google Services Plugin due to detected Google Play Build!")
    apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
} else {
    logger.lifecycle("🚫 Google Services Plugin is NOT applied for this variant.")
    // Disable DependencyInfoBlock for fdroid builds
    android {
        dependenciesInfo {
            includeInApk = false
            includeInBundle = false
        }
    }
}

dependencies {
    implementation(projects.uikit)
    implementation(projects.common.async)
    implementation(projects.core.navigation)

    implementation(projects.utils.files)
    implementation(projects.utils.filesApi)
    implementation(projects.utils.dateTime)
    implementation(projects.utils.dateTimeApi)
    implementation(projects.utils.ui)

    implementation(projects.data.db)
    implementation(projects.data.dbApi)
    implementation(projects.data.storage)
    implementation(projects.data.backupImpl)
    implementation(projects.data.backupApi)
    implementation(projects.data.backupDb)
    implementation(projects.data.cleanerApi)
    implementation(projects.data.cleanerImpl)

    implementation(projects.feature.docgroup.db)
    implementation(projects.feature.docgroup.manager)
    implementation(projects.feature.docgroup.list)
    implementation(projects.feature.docgroup.details)
    implementation(projects.feature.docgroup.domain)
    implementation(projects.feature.docgroup.repoApi)
    implementation(projects.feature.docgroup.repoImpl)
    implementation(projects.feature.docgroup.navigation)
    implementation(projects.feature.docgroup.uiApi)
    implementation(projects.feature.docgroup.uiImpl)

    implementation(projects.feature.docs.db)
    implementation(projects.feature.docs.manager)
    implementation(projects.feature.docs.list)
    implementation(projects.feature.docs.details)
    implementation(projects.feature.docs.domain)
    implementation(projects.feature.docs.navigation)
    implementation(projects.feature.docs.repoApi)
    implementation(projects.feature.docs.repoImpl)
    implementation(projects.feature.docs.uiApi)
    implementation(projects.feature.docs.uiImpl)

    implementation(projects.feature.settings.ui)
    implementation(projects.feature.settings.navigation)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.androidx.runtime.compose)

    coreLibraryDesugaring(libs.android.desugarJdkLibs)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.robolectric)

    implementation(libs.coil)
    implementation(libs.timber)
}

moduleGraphAssert {
    maxHeight = 6
    assertOnAnyBuild = true
}
