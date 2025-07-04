@file:OptIn(ExperimentalWasmDsl::class)

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.binaryCompatibility)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.publish)
}

kotlin {
    explicitApi()

    androidTarget()

    jvm()

    wasmJs {
        browser()
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeShadows"
            isStatic = true
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain.dependencies {
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "dev.lennartegb.shadows"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
    testOptions {
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
}

val localProperties = gradleLocalProperties(rootDir, providers)

publishing {
    repositories {
        maven {
            name = "githubPackages"
            url = uri("https://maven.pkg.github.com/lennartegb/compose-shadow")
            credentials {
                username = localProperties.getProperty("githubPackagesUsername")
                password = localProperties.getProperty("githubPackagesPassword")
            }
        }
    }
}

mavenPublishing {
    coordinates("dev.lennartegb.compose", "shadow", "0.1.3")
}
