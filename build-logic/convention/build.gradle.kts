import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.grappim.docuvault.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "docuvault.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidLibrary") {
            id = "docuvault.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "docuvault.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("kotlinSerialization") {
            id = "docuvault.kotlin.serialization"
            implementationClass = "KotlinSerializationConventionPlugin"
        }
        register("javaLibrary") {
            id = "docuvault.java.library"
            implementationClass = "JavaLibraryConventionPlugin"
        }
        register("androidApplication") {
            id = "docuvault.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}
