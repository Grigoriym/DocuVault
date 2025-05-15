pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// https://issuetracker.google.com/issues/315023802#comment18
gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "DocuVault"

include(":app")
include(":uikit")
include(":common:async")
include(":testing")
include(":core:navigation")
include(
    ":utils:files",
    ":utils:files-api",
    ":utils:date-time",
    ":utils:date-time-api",
    ":utils:ui"
)
include(
    ":data:cleaner-api",
    ":data:cleaner-impl",
    ":data:db-api",
    ":data:storage",
    ":data:db"
)
include(
    ":feature:docs:db",
    ":feature:docs:manager",
    ":feature:docs:list",
    ":feature:docs:details",
    ":feature:docs:repo-impl",
    ":feature:docs:repo-api",
    ":feature:docs:navigation",
    ":feature:docs:domain",
    ":feature:docs:ui-impl",
    ":feature:docs:ui-api"
)
include(
    ":feature:docgroup:db",
    ":feature:docgroup:manager",
    ":feature:docgroup:ui-api",
    ":feature:docgroup:ui-impl",
    ":feature:docgroup:navigation",
    ":feature:docgroup:repo-impl",
    ":feature:docgroup:repo-api",
    ":feature:docgroup:domain",
    ":feature:docgroup:details",
    ":feature:docgroup:list"
)
include(
    ":data:backup-api",
    ":data:backup-impl",
    ":data:backup-db"
)
include(
    ":feature:settings:ui",
    ":feature:settings:navigation"
)

check(JavaVersion.current().isCompatibleWith(JavaVersion.VERSION_17)) {
    """
    DocuVault requires JDK 17+ but it is currently using JDK ${JavaVersion.current()}.
    Java Home: [${System.getProperty("java.home")}]
    https://developer.android.com/build/jdks#jdk-config-in-studio
    """.trimIndent()
}
