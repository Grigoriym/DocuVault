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
