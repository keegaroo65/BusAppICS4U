import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val localProperties = gradleLocalProperties(rootDir, providers)
val mapboxKey: String = localProperties.getProperty("mapboxKey")
val ocKey: String = localProperties.getProperty("ocKey")

plugins {
    id("com.android.application")
    id("com.google.devtools.ksp") version "2.3.2"
    alias(libs.plugins.kotlin.compose)
}

configure<com.android.build.api.dsl.ApplicationExtension> {
    namespace = "com.example.busappics4u"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.busappics4u"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        //android.buildFeatures.buildConfig = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            buildConfigField("String", "mapboxKey", mapboxKey)
            buildConfigField("String", "ocKey", ocKey)

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "mapboxKey", mapboxKey)
            buildConfigField("String", "ocKey", ocKey)
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Android dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
//    implementation("androidx.compose.material3:material3")
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.material3.android)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // External dependencies
    implementation("org.mobilitydata","gtfs-realtime-bindings","0.0.8")
    testImplementation(libs.junit)
}