import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

val mapboxAccessToken: String by project
val mapboxStyleUrl = "mapbox://styles/yutkachev/cm0fetjsp000201qy8u933dj0"

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "map"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.mapbox.core)
            implementation(libs.mapbox.compose)
        }
        commonMain.dependencies {
            implementation(projects.coreFeatureApi)
            implementation(projects.coreUi)

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.koin.core)
            implementation(libs.compose.navigation)
            implementation(libs.orbit.compose)
        }
    }
}

android {
    namespace = "ru.spacestar.map"
    compileSdk = libs.versions.android.targetSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    buildTypes {
        debug {
            buildConfigField("String", "mapboxAccessToken", "\"$mapboxAccessToken\"")
            buildConfigField("String", "mapboxStyleUrl", "\"$mapboxStyleUrl\"")
        }
        release {
            buildConfigField("String", "mapboxAccessToken", "\"$mapboxAccessToken\"")
            buildConfigField("String", "mapboxStyleUrl", "\"$mapboxStyleUrl\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}
