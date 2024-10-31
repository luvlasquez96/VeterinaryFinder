// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val navVersion = "2.8.2"
    val kotlin_version = "1.8.0"
    val compose_version = "1.5.1"

    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
    }
}

allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlin") {
                useVersion("1.9.0")
            }
        }
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
    id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false
    id("com.android.library") version "8.2.2" apply false
}