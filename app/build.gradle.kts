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
    implementation(project(":uikit"))
    implementation(project(":utils:files"))
    implementation(project(":common:async"))
    implementation(project(":data:db"))
    implementation(project(":data:storage"))
    implementation(project(":utils:date-time"))
    implementation(project(":utils:date-time-api"))
    implementation(project(":utils:ui"))
    implementation(project(":core:navigation"))

    implementation(project(":feature:group:manager"))
    implementation(project(":feature:group:list"))
    implementation(project(":feature:group:details"))
    implementation(project(":feature:group:domain"))
    implementation(project(":feature:group:db"))
    implementation(project(":feature:group:repo-impl"))
    implementation(project(":feature:group:repo-api"))
    implementation(project(":feature:group:navigation"))
    implementation(project(":feature:group:ui-api"))
    implementation(project(":feature:group:ui-impl"))

    implementation(project(":feature:docs:db"))
    implementation(project(":feature:docs:manager"))
    implementation(project(":feature:docs:list"))
    implementation(project(":feature:docs:details"))
    implementation(project(":feature:docs:domain"))
    implementation(project(":feature:docs:navigation"))
    implementation(project(":feature:docs:repo-impl"))
    implementation(project(":feature:docs:repo-api"))

    implementation(project(":feature:settings:ui"))
    implementation(project(":feature:settings:navigation"))

    implementation(project(":data:backup-impl"))
    implementation(project(":data:backup-api"))
    implementation(project(":data:backup-db"))

    implementation(project(":data:cleaner-api"))
    implementation(project(":data:cleaner-impl"))

    implementation(project(":utils:files-api"))

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

//    testImplementation(kotlin("test"))
//    androidTestImplementation(kotlin("test"))
//    testImplementation(project(":testing"))
//    androidTestImplementation(project(":testing"))

    testImplementation(libs.robolectric)

    implementation(libs.coil)
    implementation(libs.timber)
}

moduleGraphAssert {
    maxHeight = 6
    assertOnAnyBuild = true
}
