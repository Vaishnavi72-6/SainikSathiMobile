pluginManagement {
    plugins {
        id("org.jetbrains.kotlin.android") version "1.9.24"
        id("org.jetbrains.kotlin.kapt") version "1.9.10"
    }
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // ✅ Required
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io") // ✅ Required again here
    }
}

rootProject.name = "SainikSathiMobile"
include(":app")
