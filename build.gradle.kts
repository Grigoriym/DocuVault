plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.gms.googleServices) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)

    alias(libs.plugins.jacocoAggregationResults)
    alias(libs.plugins.jacocoAggregationCoverage)
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    detekt {
        parallel = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        allRules = false
    }

    ktlint {
        android = true
        ignoreFailures = false
        reporters {
            reporterReporterType.HTML)
        }
    }

    tasks.withType<Test> {
        failFast = true
        reports {
            html.required.set(true)
        }
        testLogging {
            events(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
            showStandardStreams = true
            exceptionFormat = TestExceptionFormat.FULL
            showExceptions = true
        }
    }
}

testAggregation {
    coverage {
        exclude(coverageExclusions)
    }
}

//buildscript {
//    apply from: 'projectConfig.gradle'
//
//    ext.versions = [
//            androidPlugin: "7.3.0",
//            timber       : "5.0.1",
//            ktor         : "2.1.2",
//            kotlin       : "1.9.10",
//            coroutines   : "1.7.3",
//
//            android      : [
//                    appcompat : "1.6.1",
//                    fragment  : "1.5.3",
//                    viewpager : "1.0.0",
//                    core      : "1.12.0",
//
//                    navigation: "2.7.3",
//                    startup   : "1.1.1",
//                    viewmodel : "2.6.2",
//                    room      : "2.6.0-rc01",
//                    worker    : "2.8.1"
//            ],
//            compose      : [
//                    core            : "1.3.0-rc01",
//                    accompanist     : "0.33.1-alpha",
//                    compiler        : "1.5.3",
//                    constraintLayout: "1.0.1",
//
//            ],
//            google       : [
//                    hilt    : "2.47",
//                    material: "1.9.0"
//            ]
//    ]
//
//    ext.deps = [
//            android   : [
//                    appcompat: "androidx.appcompat:appcompat:${versions.android.appcompat}",
//                    fragment : "androidx.fragment:fragment-ktx:${versions.android.fragment}",
//                    viewpager: "androidx.viewpager2:viewpager2:${versions.android.viewpager}"
//            ],
//            navigation: [
//                    fragment: "androidx.navigation:navigation-fragment-ktx:${versions.android.navigation}",
//                    ui      : "androidx.navigation:navigation-ui-ktx:${versions.android.navigation}"
//            ],
//    ]
//
//    dependencies {
//        classpath("com.google.dagger:hilt-android-gradle-plugin:${versions.google.hilt}")
//        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.10")
//        classpath("com.google.gms:google-services:4.4.0")
//        classpath 'com.google.firebase:firebase-crashlytics-gradle:2.9.9'
//    }
//}
//
//plugins {
//    id "com.android.application" version "8.1.1" apply false
//    id 'com.android.library' version '8.1.1' apply false
//    id 'org.jetbrains.kotlin.android' version '1.9.10' apply false
//    id 'org.jetbrains.kotlin.jvm' version '1.9.10' apply false
//}
//
//apply from: 'projectConfig.gradle'