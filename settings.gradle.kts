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
    }
}

// https://issuetracker.google.com/issues/315023802#comment18
gradle.startParameter.excludedTaskNames.addAll(listOf(":build-logic:convention:testClasses"))

rootProject.name = "DocuVault"

include(":app")
include(":uikit")
include(":data:storage")
include(":data:db")
include(":common:async")
include(":testing")
include(":utils:files")
include(":utils:date-time")
include(":utils:ui")
include(":core:navigation")
include(":feature:group:manager")
include(":feature:group:list")
include(":feature:group:details")
include(":feature:group:domain")
include(":feature:group:repo-api")
include(":feature:group:repo-impl")
include(":feature:group:db")
include(":feature:group:navigation")
include(
    ":feature:docs:db",
    ":feature:docs:manager",
    ":feature:docs:list",
    ":feature:docs:list",
    ":feature:docs:details",
    ":feature:docs:repo-impl",
    ":feature:docs:repo-api",
    ":feature:docs:navigation",
    ":feature:docs:domain"
)
include(
    ":data:backup-api",
    ":data:backup-impl",
    ":data:backup-db"
)
include(":data:cleaner-api")
include(":data:cleaner-impl")
include(":data:db-api")
include(":utils:files-api")
include(":feature:group:ui-api")
include(":feature:group:ui-impl")
include(":feature:docs:ui-api")
include(":feature:docs:ui-impl")
include(":utils:date-time-api")
