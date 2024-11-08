import com.grappim.docuvault.buildlogic.DocuVaultBuildTypes

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gms.googleServices)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.docuvault.android.hilt)
    alias(libs.plugins.moduleGraphAssertion)
}

android {
    namespace = "com.grappim.docuvault"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.grappim.docuvault"
        testApplicationId = "com.grappim.docuvault.tests"

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = DocuVaultBuildTypes.DEBUG.applicationIdSuffix
        }
        release {
            applicationIdSuffix = DocuVaultBuildTypes.RELEASE.applicationIdSuffix

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true

        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"

        freeCompilerArgs += "-opt-in=androidx.compose.material.ExperimentalMaterialApi"
        freeCompilerArgs += "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi"
        freeCompilerArgs += "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi"
        freeCompilerArgs += "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeKotlinCompiler.get()
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
    packaging.resources.excludes.apply {
        add("META-INF/AL2.0")
        add("META-INF/LGPL2.1")
        add("META-INF/LICENSE.md")
        add("META-INF/LICENSE-notice.md")
        add("META-INF/ASL2.0")
        add("META-INF/notice.txt")
        add("META-INF/NOTICE.txt")
        add("META-INF/NOTICE")
        add("META-INF/license.txt")
        add("DEPENDENCIES")
    }
    bundle {
        language {
            enableSplit = false
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
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.google.inAppUpdate)
    implementation(libs.google.inAppUpdateKtx)

    testImplementation(kotlin("test"))
    androidTestImplementation(kotlin("test"))
    testImplementation(project(":testing"))
    androidTestImplementation(project(":testing"))

    testImplementation(libs.robolectric)

    implementation(libs.coil)
    implementation(libs.timber)
}

moduleGraphAssert {
    maxHeight = 6
    assertOnAnyBuild = true
}
