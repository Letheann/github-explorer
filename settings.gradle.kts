@Suppress("DSL_SCOPE_VIOLATION")
pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("org.jetbrains.kotlin.android") version "2.0.0"
        id("org.jetbrains.kotlin.compose.compiler") version "2.0.0"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "githubexplorer"
include(":app")
include(":shared")
