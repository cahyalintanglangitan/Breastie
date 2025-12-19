pluginManagement {
    repositories {
        google()              // ❗ TANPA FILTER
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()              // ❗ TANPA FILTER
        mavenCentral()
    }
}

rootProject.name = "backup"
include(":app")
