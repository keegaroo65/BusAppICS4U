import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

val mapboxKey: String = gradleLocalProperties(rootDir).getProperty("mapboxKey")
val ocKey: String = gradleLocalProperties(rootDir).getProperty("ocKey")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.busappics4u"
    compileSdk = 34

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

configurations.all {
    resolutionStrategy {
        force("org.xerial:sqlite-jdbc:3.45.3.0")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    /*implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.js)
    implementation(libs.ktor.client.contentNegotiation)
    implementation(libs.ktor.client.webSockets)
    implementation(libs.ktor.serialization.json)

    implementation(npm("mapbox-gl", libs.versions.mapboxGl.get()))*/
    //implementation("ca.derekellis.reroute", "ca.derekellis.kgtfs","gtfs")
    implementation("org.mobilitydata","gtfs-realtime-bindings","0.0.8")

    // for kgtfs
    api(libs.exposed.core)

    implementation(libs.okhttp)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.sqldelight)
    implementation(libs.csv)

    implementation(libs.exposed.core)
    implementation(libs.exposed.javaTime)
    implementation(libs.exposed.jdbc)

    api(libs.spatialk.turf)

    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.kotlinx.coroutines.test)

    // bug fix for kgtfs?
    //implementation("org.xerial","sqlite-jdbc","3.45.3.0")
}

