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
include(":domain")
include(":data:storage")
include(":data:repo")
include(":data:db")
include(":common:async")
include(":testing")
include(":utils:files")
include(":utils:date-time")
include(":feature:docmanager:ui")
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
