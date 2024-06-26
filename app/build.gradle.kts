import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    kotlin("plugin.serialization") version "2.0.0"

}


fun getLocalMapboxApiKey(): String {
    val properties = Properties()
    FileInputStream("local.properties").use { properties.load(it) }
    return properties.getProperty("MAPBOX_API_KEY", "")
}

val mapboxApiKey = getLocalMapboxApiKey()

android {
    signingConfigs {
        create("release") {
            storeFile = file("C:\\ProjectX\\explory.jks")
            storePassword = "121212"
            keyAlias = "key0"
            keyPassword = "121212"
        }
    }
    namespace = "com.example.explory"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.explory"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildFeatures {
            buildConfig = true
        }
        buildConfigField("String", "MAPBOX_API_KEY", "\"${mapboxApiKey}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)

    implementation(libs.insert.koin.koin.android)
    implementation(libs.koin.androidx.compose)

    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.squareup.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.mapbox.android) {
        exclude(
            group = "com.mapbox.plugin",
            module = "attribution"
        )
        exclude(
            group = "com.mapbox.plugin",
            module = "scalebar"
        )
        exclude(
            group = "com.mapbox.plugin",
            module = "logo"
        )
        exclude(
            group = "com.mapbox.extension",
            module = "androidauto"
        )
        exclude(
            group = "com.mapbox.extension",
            module = "localization"
        )
        exclude(
            group = "com.mapbox.extension",
            module = "compose"
        )
    }
    implementation(libs.maps.compose)
    implementation(libs.material.icons)
    implementation(libs.androidx.compose.animation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.coil)
    implementation(libs.glide)

    implementation(libs.androidx.foundation)

    implementation(libs.lottie.compose)

    implementation(libs.compose.ratingbar)

    implementation(libs.androidx.activity)
    implementation(libs.jetbrains.kotlinx.serialization.json)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.security.crypto.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}